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

package io.github.breninsul.servlet.logging2

import io.github.breninsul.servlet.logging2.filter.ServletLoggerService
import io.github.breninsul.servlet.logging2.filter.ServletLoggingFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.server.servlet.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.HandlerMapping

@ConditionalOnProperty(value = ["servlet.logging-interceptor.enabled"], havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties(ServletLoggerProperties::class)
open class ServletLoggerConfiguration {
    @Bean(name = ["ServletLoggerService"], value = ["ServletLoggerService"])
    @ConditionalOnMissingBean(name = ["ServletLoggerService"])
    fun servletLoggerService(properties: ServletLoggerProperties): ServletLoggerService {
        return ServletLoggerService(properties)
    }


    @Bean(name = ["ServletLoggingFilter"], value = ["ServletLoggingFilter"])
    @ConditionalOnClass(ServletWebServerFactory::class)
    @ConditionalOnMissingBean(name = ["ServletLoggingFilter"])
    fun loggingFilter(
        service: ServletLoggerService,
        properties: ServletLoggerProperties,
        handlerMappings: List<HandlerMapping>
    ): FilterRegistrationBean<ServletLoggingFilter> {

        val registrationBean = FilterRegistrationBean<ServletLoggingFilter>()
        val loggingFilter = ServletLoggingFilter(servletLoggerService = service, properties = properties, handlerMappings = handlerMappings)
        registrationBean.setFilter(loggingFilter)
        registrationBean.setOrder(loggingFilter.order)
        return registrationBean
    }


}
