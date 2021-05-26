package com.cgm.experiments.blogapplicationdsl

import ServerPortCustomizer
import com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers.ArticlesHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router
import java.util.*


@SpringBootApplication

class BlogApplicationDslApplication
fun main(args: Array<String>) {
    start(args)
}

fun start(args: Array<String> = emptyArray()): ConfigurableApplicationContext {

    return runApplication<BlogApplicationDslApplication>(*args){
        setDefaultProperties(
            Collections
                .singletonMap<String, Any>("server.port", (10000 ..10500).random())
        )
        addInitializers(beans {
            bean {
                router {
                    "api".nest {
                        GET("/articles", ArticlesHandler::find)
                        GET("/articles/{id}", ArticlesHandler::find)
                        DELETE("/articles/{id}", ArticlesHandler::remove)
                        accept(MediaType.APPLICATION_JSON).nest {
                            POST("/articles",ArticlesHandler::new)
                            PUT("/articles/{id}",ArticlesHandler::update)
                        }
                    }
                }
            }
        })
    }
}