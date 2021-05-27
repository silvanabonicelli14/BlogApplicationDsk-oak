package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers.ArticlesHandler
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router


fun initializeContext(): BeanDefinitionDsl = beans {
    articlesRoutes()
}

fun BeanDefinitionDsl.articlesRoutes() {
    bean {
        router {
            "api".nest {
                GET("/articles", ArticlesHandler::find)
                GET("/articles/{id}", ArticlesHandler::find)
                DELETE("/articles/{id}", ArticlesHandler::remove)
                accept(MediaType.APPLICATION_JSON).nest {
                    POST("/articles", ArticlesHandler::new)
                    PUT("/articles/{id}", ArticlesHandler::update)
                }
            }
        }
    }
}