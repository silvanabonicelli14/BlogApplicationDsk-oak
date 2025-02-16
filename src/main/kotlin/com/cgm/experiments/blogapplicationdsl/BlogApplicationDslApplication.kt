package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.utils.ServerPort
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.bind.annotation.ControllerAdvice

@SpringBootApplication()
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class,SecurityAutoConfiguration::class])
class BlogApplicationDslApplication


fun main(args: Array<String>) {
    start(FixedServerPort(8080),args)
}

fun start(
    port: ServerPort,
    args: Array<String> = emptyArray(),
    initializer: (() -> BeanDefinitionDsl?)? = null
): ConfigurableApplicationContext {

    return runApplication<BlogApplicationDslApplication>(*args) {
        setDefaultProperties(mapOf("server.port" to port.value()))
        addInitializers(initializer
            ?.run { initializer() }
            ?: initializeContext())
    }
}