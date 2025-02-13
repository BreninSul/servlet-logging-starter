package io.github.breninsul.servlet.logging2.examples

import io.github.breninsul.logging2.FormBodyType
import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.logging2.JsonBodyType
import io.github.breninsul.servlet.logging2.*
import io.github.breninsul.servlet.logging2.route.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse


@Configuration
class ExampleKotlinRoutes {
    @Bean
    fun exampleAttributesBuilderRouteKotlin(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .requestLoggingLevel(JavaLoggingLevel.INFO)
            .logRequestId(true)
            .logRequestUri(true)
            .logRequestTookTime(true)
            .logRequestHeaders(true)
            .logRequestBody(true)
            .responseLoggingLevel(JavaLoggingLevel.INFO)
            .logResponseId(true)
            .logResponseUri(true)
            .logResponseTookTime(true)
            .logResponseHeaders(true)
            .logResponseBody(true)
            .loggingRequestMaskQueryParameters(listOf("tempToken"))
            .loggingResponseMaskQueryParameters(listOf("tempToken"))
            .loggingRequestMaskHeaders(listOf("Authorization"))
            .loggingResponseMaskHeaders(listOf("ServerInfo"))
            .loggingRequestMaskBodyKeys(
                mapOf(
                    JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                    FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                )
            )
            .loggingResponseMaskBodyKeys(
                mapOf(
                    JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                    FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                )
            )
            //Add filter to log request if body is not beeng read
            .logRequest()
            .POST("/example-attributes-route-kotlin-builder") { serverRq ->
                return@POST ServerResponse
                    .ok()
                    .header("ServerInfoHeader", "SecretInfo")
                    .body(mapOf("password" to "1234"))
            }
            .build()
    }

    @Bean
    fun exampleProcessorBuilderRouteKotlin(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .processRequest {
                it.requestLoggingLevel(JavaLoggingLevel.INFO)
                it.logRequestId(true)
                it.logRequestUri(true)
                it.logRequestTookTime(true)
                it.logRequestHeaders(true)
                it.logRequestBody(true)
                it.responseLoggingLevel(JavaLoggingLevel.INFO)
                it.logResponseId(true)
                it.logResponseUri(true)
                it.logResponseTookTime(true)
                it.logResponseHeaders(true)
                it.logResponseBody(true)
                it.loggingRequestMaskQueryParameters(listOf("tempToken"))
                it.loggingResponseMaskQueryParameters(listOf("tempToken"))
                it.loggingRequestMaskHeaders(listOf("Authorization"))
                it.loggingResponseMaskHeaders(listOf("ServerInfo"))
                it.loggingRequestMaskBodyKeys(
                    mapOf(
                        JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                        FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                    )
                )
                it.loggingResponseMaskBodyKeys(
                    mapOf(
                        JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                        FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                    )
                )
            }
            .POST("/example-attributes-route-kotlin-builder") { serverRq ->
                return@POST ServerResponse
                    .ok()
                    .header("ServerInfoHeader", "SecretInfo")
                    .body(mapOf("password" to "1234"))
            }
            .build()
    }

    @Bean
    fun exampleAttributesBeforeRouteKotlin(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .before { rq ->
                rq.requestLoggingLevel(JavaLoggingLevel.INFO)
                rq.logRequestId(true)
                rq.logRequestUri(true)
                rq.logRequestTookTime(true)
                rq.logRequestHeaders(true)
                rq.logRequestBody(true)
                rq.responseLoggingLevel(JavaLoggingLevel.INFO)
                rq.logResponseId(true)
                rq.logResponseUri(true)
                rq.logResponseTookTime(true)
                rq.logResponseHeaders(true)
                rq.logResponseBody(true)
                rq.loggingRequestMaskQueryParameters(listOf("tempToken"))
                rq.loggingResponseMaskQueryParameters(listOf("tempToken"))
                rq.loggingRequestMaskHeaders(listOf("Authorization"))
                rq.loggingResponseMaskHeaders(listOf("ServerInfo"))
                rq.loggingRequestMaskBodyKeys(
                    mapOf(
                        JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                        FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                    )
                )
                rq.loggingResponseMaskBodyKeys(
                    mapOf(
                        JsonBodyType to listOf("yourJsonPropertyNameToMask", "password"),
                        FormBodyType to listOf("yourFormFieldyNameToMask", "authorisation")
                    )
                )
                return@before rq
            }
            .POST("/example-attributes-route-kotlin-before") { serverRq ->
                return@POST ServerResponse
                    .ok()
                    .header("ServerInfoHeader", "SecretInfo")
                    .body(mapOf("password" to "1234"))
            }
            .build()
    }
}