package io.github.breninsul.servlet.logging2

import io.github.breninsul.servlet.logging2.filter.ServletLoggingFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.*

@SpringBootApplication(
    scanBasePackageClasses = [ServletLoggerConfiguration::class, TestRoute::class,TestController::class]
)
class TestApplication

fun main(args: Array<String>) {


    val formatter: Formatter = object : SimpleFormatter() {
        @Synchronized
        override fun format(lr: LogRecord): String {
            return lr.level.name + "|" + lr.message
        }

    }
    val logger: Logger = Logger.getLogger(ServletLoggingFilter::class.java.name)
    logger.setUseParentHandlers(false)

    val consoleHandler = ConsoleHandler()
    consoleHandler.setFormatter(formatter);

    logger.addHandler(consoleHandler);
    runApplication<TestApplication>(*args)
}
