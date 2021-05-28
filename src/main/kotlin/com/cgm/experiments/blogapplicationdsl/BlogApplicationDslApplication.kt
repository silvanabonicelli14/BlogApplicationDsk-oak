package com.cgm.experiments.blogapplicationdsl

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.BeanDefinitionDsl


@SpringBootApplication

@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
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

//TODO "Da spostare"
interface ServerPort {
    fun value(): Int
}
data class FixedServerPort(private val value: Int): ServerPort {
    constructor(): this(RandomServerPort.value())
    override fun value(): Int = value
}
object RandomServerPort : ServerPort {

    override fun value(): Int = (10000..10500).random()

}
