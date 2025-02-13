/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.servlet.logging2.filter

import io.github.breninsul.logging2.*
import io.github.breninsul.servlet.caching.request.ServletCachingRequestWrapper
import io.github.breninsul.servlet.caching.request.ServletCachingRequestWrapperByteArray
import io.github.breninsul.servlet.caching.request.ServletCachingRequestWrapperFile
import io.github.breninsul.servlet.logging2.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Part
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.Charset
import java.util.function.Function
import java.util.function.Supplier
import java.util.logging.Level
import java.util.logging.Logger

open class ServletLoggerService(
    protected open val properties: ServletLoggerProperties,
    uriMaskerCreateFunction: Function<Collection<String>, Collection<HttpUriMasking>> = java.util.function.Function { listOf(HttpRegexUriMasking(it)) },
    bodyMaskerCreateFunctions: Map<HttpBodyType, Function<Collection<String>, Collection<HttpBodyMasking>>> = mapOf(
        JsonBodyType to java.util.function.Function { listOf(HttpRegexJsonBodyMasking(it)) },
        FormBodyType to Function { listOf(HttpRegexFormBodyMasking(it)) },
    )
) {
    protected open val helper = HttpLoggingHelper(
        "Servlet", properties.toHttpLoggingProperties(),
        uriMaskerCreateFunction,
        bodyMaskerCreateFunctions,
    )

    public fun getIdString() = helper.getIdString()

    /**
     * Log the request details.
     *
     * @param request The HttpServletRequest object representing the request.
     */
    open fun logRequest(
        request: HttpServletRequest,
    ) {
        val loggingLevel = (request.requestLoggingLevel() ?: request.loggingLevel() ?: properties.request.loggingLevel).javaLevel
        if (loggingLevel == Level.OFF) {
            return
        }
        val rqId = request.getAttribute(RQ_ID_ATTRIBUTE)?.toString() ?: ""
        val startTime = (request.getAttribute(START_TIME_ATTRIBUTE) as Long?) ?: System.currentTimeMillis()
        val maxBodySize = request.loggingRequestMaxBodySize()?.toInt() ?: properties.request.maxBodySize
        val contentLengthBeforeInit = request.contentLengthLong
        val haveToLogBody = (request.logRequestBody() ?: properties.request.bodyIncluded) && contentLengthBeforeInit < maxBodySize
        val isMultipart = request.isMultipart()
        if (haveToLogBody && request is ServletLogOnReadRequest) {
            request.initRead()
        }
        try {
            val logString =
                constructRqBody(rqId, request, startTime) {

                    val contentLength = request.contentLengthLong
                    val emptyBody = contentLength == 0L

                    if (contentLength > maxBodySize && !isMultipart) {
                        return@constructRqBody helper.constructTooBigMsg(contentLength)
                    } else if (!haveToLogBody) {
                        return@constructRqBody ""
                    } else if (emptyBody) {
                        return@constructRqBody ""
                    }

                    if (isMultipart) {
                        val parts: List<Part> = ArrayList(request.parts)
                        val loggedParts =
                            parts
                                .joinToString(";") {
                                    val size = it.size
                                    if (size >= maxBodySize) {
                                        return@joinToString "${it.name}:${it.partContentType()}:${helper.constructTooBigMsg(size)}"
                                    } else {
                                        val partContent =
                                            if (it.haveToLogContent()) {
                                                if (request !is ServletLogOnReadRequest) {
                                                    throw IllegalStateException("Cached request is required for body logging")
                                                }
                                                String(it.inputStream.use { i -> i.readAllBytes() }, request.getContentEncoding())
                                            } else {
                                                "<FILE $size bytes>"
                                            }
                                        return@joinToString "${it.name}:${it.partContentType()}:$partContent"
                                    }
                                }
                        return@constructRqBody loggedParts
                    } else {
                        if (request !is ServletLogOnReadRequest) {
                            throw IllegalStateException("Cached request is required for body logging")
                        }
                        return@constructRqBody request.bodyContentString()
                    }
                }
            if (haveToLogBody && request is ServletCachingRequestWrapper) {
                request.reInitInputStream()
            }
            servletLogger.log(loggingLevel, logString)
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "", t)
        }
    }

    /**
     * Constructs the request body log message.
     *
     * @param rqId The request ID.
     * @param request The request object.
     * @param time The time taken for the request.
     * @param contentSupplier The request body Supplier.
     * @return The constructed request body log message.
     */
    protected open fun constructRqBody(
        rqId: String,
        request: HttpServletRequest,
        time: Long,
        contentSupplier: Supplier<String?>,
    ): String {
        val type = HttpLoggingHelper.Type.REQUEST
        val message =
            listOf(
                helper.getHeaderLine(type),
                helper.getIdString(request.logRequestId(), rqId, type),
                helper.getUriString(request.loggingRequestMaskQueryParameters(), request.logRequestUri(), "${request.method} ${request.getFormattedUriString()}", type),
                helper.getTookString(request.logRequestTookTime(), time, type),
                helper.getHeadersString(request.loggingRequestMaskHeaders(), request.logRequestHeaders(), request.getHeadersMultiMap(), type),
                helper.getBodyString(request.loggingRequestMaskBodyKeys(), request.logRequestBody(), contentSupplier, type),
                helper.getFooterLine(type),
            ).filter { !it.isNullOrBlank() }
                .joinToString("\n")
        return message
    }

    open fun afterChain(
        rqId: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        startTime: Long,
    ) {
        try {
            val loggingLevel = properties.response.loggingLevel.javaLevel
            if (loggingLevel == Level.OFF) {
                return
            }
            response.setHeader("RQ_ID", rqId)
            logResponse(request, response, rqId, startTime)
            if (response is ContentCachingResponseWrapper) {
                response.copyBodyToResponse()
            }
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "Logging servlet response error !", t)
        }
    }


    /**
     * Logs the HTTP response.
     *
     * @param request
     */
    protected open fun logResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        rqId: String,
        time: Long,
    ) {
        val loggingLevel = (request.responseLoggingLevel() ?: request.loggingLevel() ?: properties.response.loggingLevel).javaLevel
        if (loggingLevel == Level.OFF) {
            return
        }
        val maxBodySize = request.loggingResponseMaxBodySize()?.toInt() ?: properties.request.maxBodySize
        val logString =
            constructRsBody(rqId, response, request, time) {
                val haveToLogBody = request.logResponseBody() ?: properties.response.bodyIncluded
                if (!haveToLogBody) {
                    return@constructRsBody ""
                }
                if (response !is ContentCachingResponseWrapper) {
                    throw IllegalStateException("Cached response is required for body logging")
                }
                val bytes = response.contentAsByteArray
                val contentString = if (bytes.size > maxBodySize) helper.constructTooBigMsg(bytes.size.toLong()) else String(bytes, Charset.forName(response.characterEncoding))
                return@constructRsBody contentString
            }
        servletLogger.log(loggingLevel, logString)
    }


    /**
     * Constructs the response body for logging.
     *
     * @param rqId The request ID.
     * @param response The HttpServletResponse response.
     * @param request The HttpServletRequest request.
     * @param time The time taken for the request/response.
     * @param contentSupplier The response body content Supplier.
     * @return The formatted response body.
     */
    protected open fun constructRsBody(
        rqId: String,
        response: HttpServletResponse,
        request: HttpServletRequest,
        time: Long,
        contentSupplier: Supplier<String?>,
    ): String {
        val type = HttpLoggingHelper.Type.RESPONSE
        val message =
            listOf(
                helper.getHeaderLine(type),
                helper.getIdString(request.logResponseId(), rqId, type),
                helper.getUriString(request.loggingResponseMaskQueryParameters(), request.logResponseUri(), "${response.status} ${request.method} ${request.getFormattedUriString()}", type),
                helper.getTookString(request.logResponseTookTime(), time, type),
                helper.getHeadersString(request.loggingResponseMaskHeaders(), request.logResponseHeaders(), response.getHeadersMultiMap(), type),
                helper.getBodyString(request.loggingResponseMaskBodyKeys(), request.logResponseBody(), contentSupplier, type),
                helper.getFooterLine(type),
            ).filter { !it.isNullOrBlank() }
                .joinToString("\n")
        return message
    }

    /**
     * Wraps the given HttpServletRequest object to provide caching
     * functionality for the request body if necessary.
     *
     * @param request The HttpServletRequest object to be wrapped.
     * @return The wrapped HttpServletRequest object.
     */
    public open fun wrapRequest(request: HttpServletRequest): HttpServletRequest {
        if ((request.requestLoggingLevel()?.javaLevel ?: request.loggingLevel() ?: properties.request.loggingLevel) == Level.OFF) {
            request.logRequestBody(false)
            return request
        }
        servletLogger.log(Level.FINE, "Came request ${request.getAttribute(RQ_ID_ATTRIBUTE)} ${request.method} ${request.requestURI}")
        if (request is ServletLogOnReadRequest) {
            return request
        }
        val haveToLogBody = request.logRequestBody() ?: properties.request.bodyIncluded
        if (!haveToLogBody) {
            request.logRequestBody(false)
//            return request
        }
        val isMultipart = request.isMultipart()

        val contentLength = if (isMultipart) request.parts.filter { it.haveToLogContent() }.minOfOrNull { it.size } ?: -1 else request.contentLengthLong
        val maxBodySize = request.loggingRequestMaxBodySize()?.toInt() ?: properties.request.maxBodySize
        if (contentLength > maxBodySize) {
            request.logRequestBody(false)
            return request
        }
        val maxSizeToKeepInMemory = request.loggingRequestMaxBodySizeToUseTempFileCaching() ?: properties.request.bodySizeToUseTempFileCaching
        val cachedRequest = if (contentLength > maxSizeToKeepInMemory) {
            ServletCachingRequestWrapperFile(request, false)
        } else {
            ServletCachingRequestWrapperByteArray(request, false)
        }
        return ServletLogOnReadRequestDelegate(cachedRequest) { rq -> logRequest(rq) };
    }

    /**
     * Wraps the given HttpServletResponse with a
     * ContentCachingResponseWrapper.
     *
     * @param response The HttpServletResponse to be wrapped.
     * @return The ContentCachingResponseWrapper that wraps the given response.
     */
    public open fun wrapResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): HttpServletResponse {
        if (helper.loggingLevel == Level.OFF) {
            return response
        }
        if (response is ContentCachingResponseWrapper) {
            return response
        }
        val haveToLogBody = request.logResponseBody() ?: properties.response.bodyIncluded
        if (!haveToLogBody) {
            return response
        }
        return ContentCachingResponseWrapper(response)
    }

    protected open val servletLogger: Logger = Logger.getLogger(ServletLoggingFilter::class.java.name)
    public open fun HttpServletRequest.isMultipart() = this.contentType != null && this.contentType.startsWith("multipart/")

    public open fun Part.partContentType(): String = this.contentType ?: "text/plain"

    public open fun Part.haveToLogContent(): Boolean = this.partContentType().startsWith("text", true)
}