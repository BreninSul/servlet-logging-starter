package io.github.breninsul.servlet.logging2.test.route

import io.github.breninsul.servlet.logging2.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.AnnotationConfigWebContextLoader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import kotlin.text.Charsets.UTF_8


@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class, AnnotationConfigWebContextLoader::class])
class RouteTestMaskAttributesChanged {

    @Test
    fun testLogMaskFormRequest() {
        val formData: MutableMap<String, String> = HashMap()
        formData["formKeyToMask"] = "SECRET"
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-route-custom-mask-2?queryParamToMask=SECRET"))
            .header("headerToMask", "SECRET")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formData.toFormDataAsString()))
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos));
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Thread.sleep(100)
        assert(!baos.toString(UTF_8).contains("SECRET"))
    }

    protected fun Map<String, String>.toFormDataAsString(): String {
        val formBodyBuilder = StringBuilder()
        for ((key, value) in this) {
            if (formBodyBuilder.length > 0) {
                formBodyBuilder.append("&")
            }
            formBodyBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
            formBodyBuilder.append("=")
            formBodyBuilder.append(URLEncoder.encode(value, StandardCharsets.UTF_8))
        }
        return formBodyBuilder.toString()
    }
}