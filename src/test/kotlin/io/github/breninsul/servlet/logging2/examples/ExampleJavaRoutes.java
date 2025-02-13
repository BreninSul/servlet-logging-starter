package io.github.breninsul.servlet.logging2.examples;

import io.github.breninsul.logging2.FormBodyType;
import io.github.breninsul.logging2.JavaLoggingLevel;
import io.github.breninsul.logging2.JsonBodyType;
import io.github.breninsul.servlet.logging2.route.LogRequestRouteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.Map;

import static io.github.breninsul.servlet.logging2.RouterConfigAttributesKt.*;

@Configuration
public class ExampleJavaRoutes {


    @Bean
    public RouterFunction<ServerResponse> exampleAttributesBeforeRouteJava() {
        return RouterFunctions
                .route()
                .before(rq -> {
                    requestLoggingLevel(rq, JavaLoggingLevel.INFO);
                    logRequestId(rq, true);
                    logRequestUri(rq, true);
                    logRequestTookTime(rq, true);
                    logRequestHeaders(rq, true);
                    logRequestBody(rq, true);
                    responseLoggingLevel(rq, JavaLoggingLevel.INFO);
                    logResponseId(rq, true);
                    logResponseUri(rq, true);
                    logResponseTookTime(rq, true);
                    logResponseHeaders(rq, true);
                    logResponseBody(rq, true);
                    loggingRequestMaskQueryParameters(rq, List.of("tempToken"));
                    loggingResponseMaskQueryParameters(rq, List.of("tempToken"));
                    loggingRequestMaskHeaders(rq, List.of("Authorization"));
                    loggingResponseMaskHeaders(rq, List.of("ServerInfo"));
                    loggingRequestMaskBodyKeys(rq, Map.of(JsonBodyType.INSTANCE, List.of("yourJsonPropertyNameToMask", "password"),
                            FormBodyType.INSTANCE, List.of("yourFormFieldyNameToMask", "authorisation")));
                    loggingResponseMaskBodyKeys(rq, Map.of(JsonBodyType.INSTANCE, List.of("yourJsonPropertyNameToMask", "password"),
                            FormBodyType.INSTANCE, List.of("yourFormFieldyNameToMask", "authorisation")));
                    return rq;
                })
                //Add filter to log request if body is not beeng read
                .filter(LogRequestRouteFilter.INSTANCE)
                .POST("/example-attributes-route-kotlin-before", serverRq ->
                        ServerResponse
                                .ok()
                                .header("ServerInfoHeader", "SecretInfo")
                                .body(Map.of("password", "1234"))
                )
                .build();
    }
}