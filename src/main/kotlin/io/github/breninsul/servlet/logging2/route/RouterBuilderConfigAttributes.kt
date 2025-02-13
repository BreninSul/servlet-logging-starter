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

package io.github.breninsul.servlet.logging2.route

import io.github.breninsul.logging2.HttpBodyType
import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.*
import org.springframework.web.servlet.function.RouterFunctions.Builder
import org.springframework.web.servlet.function.ServerRequest
import java.util.function.Consumer

/**
 * Adds a filter to the route builder that logs the request when the body
 * has not been read.
 *
 * The filter ensures that the request body is logged during the
 * request-handling process if it hasn't already been logged (If
 * InputStream is not readen). This can be useful for debugging or
 * monitoring purposes.
 *
 * @return The updated `Builder` instance with the `LogRequestRouteFilter`
 *    applied.
 */
fun Builder.logRequest(): Builder = this.filter(LogRequestRouteFilter)

fun Builder.processRequest(consumer: Consumer<ServerRequest>) = this
    .before { rq ->
        consumer.accept(rq)
        return@before rq
    }

fun Builder.loggingEnabled(enable: Boolean?) = this.processRequest { it.servletRequest().loggingEnabled(enable) }

fun Builder.loggingLevel(level: JavaLoggingLevel?) = this.processRequest { it.servletRequest().loggingLevel(level) }

fun Builder.requestLoggingLevel(level: JavaLoggingLevel?) =
    this.processRequest { it.servletRequest().requestLoggingLevel(level) }

fun Builder.logRequestId(enable: Boolean?) = this.processRequest { it.servletRequest().logRequestId(enable) }

fun Builder.logRequestUri(enable: Boolean?) = this.processRequest { it.servletRequest().logRequestUri(enable) }

fun Builder.logRequestHeaders(enable: Boolean?) = this.processRequest { it.servletRequest().logRequestHeaders(enable) }

fun Builder.logRequestBody(enable: Boolean?) = this.processRequest { it.servletRequest().logRequestBody(enable) }

fun Builder.logRequestTookTime(enable: Boolean?) =
    this.processRequest { it.servletRequest().logRequestTookTime(enable) }

fun Builder.loggingRequestMaxBodySize(size: Long?) =
    this.processRequest { it.servletRequest().loggingRequestMaxBodySize(size) }

fun Builder.loggingRequestMaxBodySizeToUseTempFileCaching(size: Long?) =
    this.processRequest { it.servletRequest().loggingRequestMaxBodySizeToUseTempFileCaching(size) }

fun Builder.loggingRequestMaskHeaders(headers: Collection<String>?) =
    this.processRequest { it.servletRequest().loggingRequestMaskHeaders(headers) }

fun Builder.loggingRequestMaskQueryParameters(queryParameters: Collection<String>?) =
    this.processRequest { it.servletRequest().loggingRequestMaskQueryParameters(queryParameters) }

fun Builder.loggingRequestMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) =
    this.processRequest { it.servletRequest().loggingRequestMaskBodyKeys(maskingKeys) }

fun Builder.responseLoggingLevel(level: JavaLoggingLevel?) =
    this.processRequest { it.servletRequest().responseLoggingLevel(level) }

fun Builder.logResponseId(enable: Boolean?) = this.processRequest { it.servletRequest().logResponseId(enable) }

fun Builder.logResponseUri(enable: Boolean?) = this.processRequest { it.servletRequest().logResponseUri(enable) }

fun Builder.logResponseHeaders(enable: Boolean?) =
    this.processRequest { it.servletRequest().logResponseHeaders(enable) }

fun Builder.logResponseBody(enable: Boolean?) = this.processRequest { it.servletRequest().logResponseBody(enable) }

fun Builder.logResponseTookTime(enable: Boolean?) =
    this.processRequest { it.servletRequest().logResponseTookTime(enable) }

fun Builder.loggingResponseMaxBodySize(size: Long?) =
    this.processRequest { it.servletRequest().loggingResponseMaxBodySize(size) }

fun Builder.loggingResponseMaxBodySizeToUseTempFileCaching(size: Long?) =
    this.processRequest { it.servletRequest().loggingResponseMaxBodySizeToUseTempFileCaching(size) }

fun Builder.loggingResponseMaskHeaders(headers: Collection<String>?) =
    this.processRequest { it.servletRequest().loggingResponseMaskHeaders(headers) }

fun Builder.loggingResponseMaskQueryParameters(queryParameters: Collection<String>?) =
    this.processRequest { it.servletRequest().loggingResponseMaskQueryParameters(queryParameters) }

fun Builder.loggingResponseMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) =
    this.processRequest { it.servletRequest().loggingResponseMaskBodyKeys(maskingKeys) }
