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

import io.github.breninsul.servlet.logging2.*
import io.github.breninsul.servlet.logging2.handlerResolver.DefaultRequestHandlerLoggingAnnotationResolver
import io.github.breninsul.servlet.logging2.handlerResolver.RequestHandlerLoggingAnnotationResolver
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerMapping
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Represents the constant key for the request attribute used to store the
 * controller information in a servlet-based logging or routing context.
 * This constant can be used to manage or retrieve metadata about the
 * controller handling the request.
 */


open class ServletLoggingFilter(
    protected open val servletLoggerService: ServletLoggerService,
    protected open val properties: ServletLoggerProperties,
    handlerMappings: List<HandlerMapping>,
    protected open val requestHandlerResolver: RequestHandlerLoggingAnnotationResolver = DefaultRequestHandlerLoggingAnnotationResolver(handlerMappings),
) : OncePerRequestFilter(),
    Ordered {


    protected open val servletLogger: Logger = Logger.getLogger(ServletLoggingFilter::class.java.name)

    override public fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val time = System.currentTimeMillis()
        request.setAttribute(START_TIME_ATTRIBUTE, time)
        try {
            if (properties.resolveHandlerAnnotation) {
                val annotationOptional = requestHandlerResolver.findAnnotationSettings(request)
                if (annotationOptional.isPresent) {
                    val annotation = annotationOptional.get()
                    //Ignore filter if logging disabled
                    if (!annotation.enabled) {
                        return filterChain.doFilter(request, response)
                    }
                    request.setAnnotationPropertiesToRequestAttributes(annotation)
                }
            }
            //Ignore filter if logging disabled
            if (!(request.loggingEnabled() ?: properties.enabled)) {
                return filterChain.doFilter(request, response)
            }
            val id = servletLoggerService.getIdString()
            request.setAttribute(RQ_ID_ATTRIBUTE, id)

            val wrappedRequest = servletLoggerService.wrapRequest(request)
            val wrappedResponse = servletLoggerService.wrapResponse(wrappedRequest, response)
            try {
                filterChain.doFilter(wrappedRequest, wrappedResponse)
                if (wrappedRequest is ServletLogOnReadRequest) {
                    wrappedRequest.performLogActionIfNotPerformedBefore()
                    wrappedRequest.clear()
                }
            } finally {
                servletLoggerService.afterChain(id, wrappedRequest, wrappedResponse, time)
            }
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "Logging servlet request error !", t)
        }
    }


    protected open fun HttpServletRequest.setAnnotationPropertiesToRequestAttributes(
        annotation: ServletLoggerProperties
    ) {
        if (this.loggingEnabled() == null) this.loggingEnabled(annotation.enabled)
        if (this.loggingLevel() == null) this.loggingLevel(annotation.loggingLevel)

        if (this.requestLoggingLevel() == null) this.requestLoggingLevel(annotation.request.loggingLevel)
        if (this.logRequestId() == null) this.logRequestId(annotation.request.idIncluded)
        if (this.logRequestUri() == null) this.logRequestUri(annotation.request.uriIncluded)
        if (this.logRequestTookTime() == null) this.logRequestTookTime(annotation.request.tookTimeIncluded)
        if (this.logRequestHeaders() == null) this.logRequestHeaders(annotation.request.headersIncluded)
        if (this.logRequestBody() == null) this.logRequestBody(annotation.request.bodyIncluded)
        if (this.loggingRequestMaskQueryParameters() == null) this.loggingRequestMaskQueryParameters(annotation.request.mask.maskQueryParameters)
        if (this.loggingRequestMaskHeaders() == null) this.loggingRequestMaskHeaders(annotation.request.mask.maskHeaders)
        if (this.loggingRequestMaskBodyKeys() == null) this.loggingRequestMaskBodyKeys(annotation.request.mask.maskBodyKeys)
        if (this.responseLoggingLevel() == null) this.responseLoggingLevel(annotation.response.loggingLevel)
        if (this.logResponseId() == null) this.logResponseId(annotation.response.idIncluded)
        if (this.logResponseTookTime() == null) this.logResponseTookTime(annotation.response.tookTimeIncluded)
        if (this.logResponseUri() == null) this.logResponseUri(annotation.response.uriIncluded)
        if (this.logResponseHeaders() == null) this.logResponseHeaders(annotation.response.headersIncluded)
        if (this.logResponseBody() == null) this.logResponseBody(annotation.response.bodyIncluded)
        if (this.loggingResponseMaskQueryParameters() == null) this.loggingResponseMaskQueryParameters(annotation.response.mask.maskQueryParameters)
        if (this.loggingResponseMaskHeaders() == null) this.loggingResponseMaskHeaders(annotation.response.mask.maskHeaders)
        if (this.loggingResponseMaskBodyKeys() == null) this.loggingResponseMaskBodyKeys(annotation.response.mask.maskBodyKeys)
    }


    override fun getOrder(): Int = properties.order
}
