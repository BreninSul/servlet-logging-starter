package io.github.breninsul.servlet.logging2.handlerResolver

import jakarta.servlet.http.HttpServletRequest

/**
 * Defines a contract for resolving the appropriate method handler (often a controller method)
 * for a given HTTP request in a web application.
 */
interface RequestHandlerLoggingAnnotationResolver {
    /**
     * Attempts to find and resolve the annotation settings for a given
     * HTTP request. The annotation settings typically provide metadata or
     * configuration for how the request should be processed or logged within
     * the system.
     *
     * @param request the HTTP request for which annotation settings are being
     *    resolved
     * @return a result containing the handler type and optional annotation
     *    properties, which may provide additional configuration for request
     *    handling
     */
    fun findHandlerSettings(request: HttpServletRequest): RequestHandlerResult

}