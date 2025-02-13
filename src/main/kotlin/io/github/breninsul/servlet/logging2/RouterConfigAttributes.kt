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

package io.github.breninsul.servlet.logging2

import io.github.breninsul.logging2.HttpBodyType
import io.github.breninsul.logging2.JavaLoggingLevel
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.servlet.function.ServerRequest

fun ServerRequest.logRequest(): HttpServletRequest? {
    val servletRequest = this.servletRequest()
    if (servletRequest is ServletLogOnReadRequest) {
        servletRequest.performLogActionIfNotPerformedBefore()
    }
    return servletRequest
}

fun ServerRequest.loggingLevel(level: JavaLoggingLevel?) = this.servletRequest().loggingLevel(level)
fun ServerRequest.loggingLevel() = this.servletRequest().loggingLevel()

fun ServerRequest.requestLoggingLevel(level: JavaLoggingLevel?) = this.servletRequest().requestLoggingLevel(level)
fun ServerRequest.requestLoggingLevel() = this.servletRequest().requestLoggingLevel()

fun ServerRequest.responseLoggingLevel(level: JavaLoggingLevel?) = this.servletRequest().responseLoggingLevel(level)
fun ServerRequest.responseLoggingLevel() = this.servletRequest().responseLoggingLevel()

fun ServerRequest.loggingEnabled(enable: Boolean?) = this.servletRequest().loggingEnabled(enable)
fun ServerRequest.loggingEnabled() = this.servletRequest().loggingEnabled()


fun ServerRequest.logRequestId() = this.servletRequest().logRequestId()
fun ServerRequest.logRequestId(enable: Boolean?) = this.servletRequest().logRequestId(enable)
fun ServerRequest.logRequestUri() = this.servletRequest().logRequestUri()
fun ServerRequest.logRequestUri(enable: Boolean?) = this.servletRequest().logRequestUri(enable)
fun ServerRequest.logRequestHeaders() = this.servletRequest().logRequestHeaders()
fun ServerRequest.logRequestHeaders(enable: Boolean?) = this.servletRequest().logRequestHeaders(enable)
fun ServerRequest.logRequestBody() = this.servletRequest().logRequestBody()
fun ServerRequest.logRequestBody(enable: Boolean?) = this.servletRequest().logRequestBody(enable)
fun ServerRequest.logRequestTookTime() = this.servletRequest().logRequestTookTime()
fun ServerRequest.logRequestTookTime(enable: Boolean?) = this.servletRequest().logRequestTookTime(enable)

fun ServerRequest.loggingRequestMaxBodySize(size: Long?) = this.servletRequest().loggingRequestMaxBodySize(size)
fun ServerRequest.loggingRequestMaxBodySize() = this.servletRequest().loggingRequestMaxBodySize()
fun ServerRequest.loggingRequestMaxBodySizeToUseTempFileCaching(size: Long?) = this.servletRequest().loggingRequestMaxBodySizeToUseTempFileCaching(size)
fun ServerRequest.loggingRequestMaxBodySizeToUseTempFileCaching() = this.servletRequest().loggingRequestMaxBodySizeToUseTempFileCaching()
fun ServerRequest.loggingRequestMaskHeaders(headers: Collection<String>?) = this.servletRequest().loggingRequestMaskHeaders(headers)
fun ServerRequest.loggingRequestMaskHeaders() = this.servletRequest().loggingRequestMaskHeaders()
fun ServerRequest.loggingRequestMaskQueryParameters(queryParameters: Collection<String>?) = this.servletRequest().loggingRequestMaskQueryParameters(queryParameters)
fun ServerRequest.loggingRequestMaskQueryParameters() = this.servletRequest().loggingRequestMaskQueryParameters()
fun ServerRequest.loggingRequestMaskBodyKeys(bodyType: HttpBodyType, keysToMask: Collection<String>?) = this.servletRequest().loggingRequestMaskBodyKeys(bodyType, keysToMask)
fun ServerRequest.loggingRequestMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) = this.servletRequest().loggingRequestMaskBodyKeys(maskingKeys)
fun ServerRequest.loggingRequestMaskBodyKeys(): Map<HttpBodyType, Collection<String>?>? = this.servletRequest().loggingRequestMaskBodyKeys()
fun ServerRequest.loggingRequestMaskBodyKeys(bodyType: HttpBodyType): Collection<String>? = this.servletRequest().loggingRequestMaskBodyKeys(bodyType)

fun ServerRequest.logResponseId() = this.servletRequest().logResponseId()
fun ServerRequest.logResponseId(enable: Boolean?) = this.servletRequest().logResponseId(enable)
fun ServerRequest.logResponseUri() = this.servletRequest().logResponseUri()
fun ServerRequest.logResponseUri(enable: Boolean?) = this.servletRequest().logResponseUri(enable)
fun ServerRequest.logResponseHeaders() = this.servletRequest().logResponseHeaders()
fun ServerRequest.logResponseHeaders(enable: Boolean?) = this.servletRequest().logResponseHeaders(enable)
fun ServerRequest.logResponseBody() = this.servletRequest().logResponseBody()
fun ServerRequest.logResponseBody(enable: Boolean?) = this.servletRequest().logResponseBody(enable)
fun ServerRequest.logResponseTookTime() = this.servletRequest().logResponseTookTime()
fun ServerRequest.logResponseTookTime(enable: Boolean?) = this.servletRequest().logResponseTookTime(enable)

fun ServerRequest.loggingResponseMaxBodySize(size: Long?) = this.servletRequest().loggingResponseMaxBodySize(size)
fun ServerRequest.loggingResponseMaxBodySize() = this.servletRequest().loggingResponseMaxBodySize()
fun ServerRequest.loggingResponseMaxBodySizeToUseTempFileCaching(size: Long?) = this.servletRequest().loggingResponseMaxBodySizeToUseTempFileCaching(size)
fun ServerRequest.loggingResponseMaxBodySizeToUseTempFileCaching() = this.servletRequest().loggingResponseMaxBodySizeToUseTempFileCaching()
fun ServerRequest.loggingResponseMaskHeaders(headers: Collection<String>?) = this.servletRequest().loggingResponseMaskHeaders(headers)
fun ServerRequest.loggingResponseMaskHeaders() = this.servletRequest().loggingResponseMaskHeaders()
fun ServerRequest.loggingResponseMaskQueryParameters(queryParameters: Collection<String>?) = this.servletRequest().loggingResponseMaskQueryParameters(queryParameters)
fun ServerRequest.loggingResponseMaskQueryParameters() = this.servletRequest().loggingResponseMaskQueryParameters()
fun ServerRequest.loggingResponseMaskBodyKeys(bodyType: HttpBodyType, keysToMask: Collection<String>?) = this.servletRequest().loggingResponseMaskBodyKeys(bodyType, keysToMask)
fun ServerRequest.loggingResponseMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) = this.servletRequest().loggingResponseMaskBodyKeys(maskingKeys)
fun ServerRequest.loggingResponseMaskBodyKeys(): Map<HttpBodyType, Collection<String>?>? = this.servletRequest().loggingResponseMaskBodyKeys()
fun ServerRequest.loggingResponseMaskBodyKeys(bodyType: HttpBodyType): Collection<String>? = this.servletRequest().loggingResponseMaskBodyKeys(bodyType)
