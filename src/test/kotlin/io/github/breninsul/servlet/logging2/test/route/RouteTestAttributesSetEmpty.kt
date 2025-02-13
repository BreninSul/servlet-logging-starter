package io.github.breninsul.servlet.logging2.test.route

import io.github.breninsul.servlet.logging2.TestApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.text.Charsets.UTF_8

@TestPropertySource(locations = ["classpath:application-test.properties"])


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class])
@ExtendWith(SpringExtension::class)
@TestConfiguration
class RouteTestAttributesSetEmpty {

    @Test
    fun testLogEmptyPropsInfoRequest() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-route-custom-config_attributes"))
            .POST(HttpRequest.BodyPublishers.ofString("Test Request", UTF_8))
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos));
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Thread.sleep(100)
        val lines = baos.toString(UTF_8.name()).lines()
        assert(lines.filter { it.contains("i.g.b.s.l.filter.ServletLoggingFilter") }.any { it.contains("ERROR") })

        val firstLine = lines.indexOf("===========================Servlet Request begin===========================")
        val realLines = lines.subList(firstLine, lines.size)
        assert(realLines[1].startsWith("===========================Servlet Request end  ==========================="))
    }

    @Test
    fun testLogEmptyPropsInfoResponse() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-route-custom-config_attributes"))
            .POST(HttpRequest.BodyPublishers.ofString("Test Request", UTF_8))
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos));
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Thread.sleep(100)
        val lines = baos.toString(UTF_8.name()).lines()
        val firstLine = lines.indexOf("===========================Servlet Response begin===========================")
        val realLines = lines.subList(firstLine, lines.size)
        assert(realLines[1].startsWith("===========================Servlet Response end  ==========================="))
    }
}