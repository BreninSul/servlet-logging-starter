package io.github.breninsul.servlet.logging2

import io.github.breninsul.logging2.FormBodyType
import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.logging2.JsonBodyType
import io.github.breninsul.servlet.logging2.route.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse


@Configuration
class TestRoute {


    @Bean
    fun testPostRoute(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .POST("/test-route") { serverRq ->
//                println("Came to test route")
                return@POST ServerResponse.ok().body("Test Response")
            }.build()
    }

    @Bean
    fun testPostRouteCustomAttributes(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .loggingLevel(JavaLoggingLevel.SEVERE)
            .requestLoggingLevel(JavaLoggingLevel.SEVERE)
            .responseLoggingLevel(JavaLoggingLevel.SEVERE)
            .logRequestId(false)
            .logRequestUri(false)
            .logRequestTookTime(false)
            .logRequestHeaders(false)
            .logRequestBody(false)
            .logResponseId(false)
            .logResponseUri(false)
            .logResponseTookTime(false)
            .logResponseHeaders(false)
            .logResponseBody(false)
            .logRequest()
            .POST("/test-route-custom-config_attributes") { serverRq ->
                return@POST ServerResponse.ok().body("Test Response")
            }.build()
    }

    @Bean
    fun testPostRouteMask(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .POST("/test-mask-properties") { serverRq ->
                return@POST ServerResponse.ok().body(mapOf("jsonKeyToMask" to "secretToMask"))
            }.build()
    }

    @Bean
    fun testPostRouteCustomMaskAttributes(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .processRequest {
                it.loggingRequestMaskQueryParameters(listOf("queryParamToMask"))
                it.loggingResponseMaskQueryParameters(listOf("queryParamToMask"))
                it.loggingRequestMaskHeaders(listOf("headerToMask"))
                it.loggingResponseMaskHeaders(listOf("headerToMask"))
                it.loggingRequestMaskBodyKeys(mapOf(JsonBodyType to listOf("jsonKeyToMask"), FormBodyType to listOf("formKeyToMask")))
                it.loggingResponseMaskBodyKeys(mapOf(JsonBodyType to listOf("jsonKeyToMask"), FormBodyType to listOf("formKeyToMask")))
            }
            .POST("/test-route-custom-mask") { serverRq ->
                val rq = serverRq.body(String::class.java)
                return@POST ServerResponse.ok()
                    .header("headerToMask", "SECRET")
                    .body(mapOf("jsonKeyToMask" to "SECRET"))
            }
            .build()
    }


    @Bean
    fun testPostRouteCustomMaskAttributes2(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .loggingRequestMaskQueryParameters(listOf("queryParamToMask"))
            .loggingResponseMaskQueryParameters(listOf("queryParamToMask"))
            .loggingRequestMaskHeaders(listOf("headerToMask"))
            .loggingResponseMaskHeaders(listOf("headerToMask"))
            .loggingRequestMaskBodyKeys(
                mapOf(
                    JsonBodyType to listOf("jsonKeyToMask"),
                    FormBodyType to listOf("formKeyToMask")
                )
            )
            .loggingResponseMaskBodyKeys(
                mapOf(
                    JsonBodyType to listOf("jsonKeyToMask"),
                    FormBodyType to listOf("formKeyToMask")
                )
            )
            .POST("/test-route-custom-mask-2") { serverRq ->
                val rq = serverRq.body(String::class.java)
                return@POST ServerResponse.ok()
                    .header("headerToMask", "SECRET")
                    .body(mapOf("jsonKeyToMask" to "SECRET"))
            }
            .build()
    }

}