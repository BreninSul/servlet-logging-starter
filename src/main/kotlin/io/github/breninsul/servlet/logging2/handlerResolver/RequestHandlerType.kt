package io.github.breninsul.servlet.logging2.handlerResolver

open class RequestHandlerType(val type: String) {
    override fun toString(): String {
        return type
    }

    override fun equals(other: Any?): Boolean {
        return other is RequestHandlerType && other.type.lowercase() == type.lowercase()
    }

    override fun hashCode(): Int {
        return type.lowercase().hashCode()
    }
}

object Router : RequestHandlerType("Router")
object Controller : RequestHandlerType("Controller")
object Unknown : RequestHandlerType("Unknown")