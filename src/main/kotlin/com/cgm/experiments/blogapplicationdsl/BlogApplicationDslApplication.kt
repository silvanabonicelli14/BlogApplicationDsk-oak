package com.cgm.experiments.blogapplicationdsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.BeanDefinitionDsl

@SpringBootApplication

class BlogApplicationDslApplication

fun main(args: Array<String>) {
    start(args)
}

fun start(
    args: Array<String> = emptyArray(),
    initializer: (() -> BeanDefinitionDsl?)? = null
): ConfigurableApplicationContext {

    return runApplication<BlogApplicationDslApplication>(*args) {
        setDefaultProperties(mapOf("server.port" to (10000..10500).random()))
        addInitializers(initializer
            ?.run { initializer() }
            ?: initializeContext())
    }
}
