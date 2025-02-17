package io.github.breninsul.servlet.logging2.handlerResolver

import io.github.breninsul.servlet.logging2.ServletLoggerProperties
import java.util.*

data class RequestHandlerResult(val type: RequestHandlerType, val annotationProperties: Optional<ServletLoggerProperties>)