package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers.ArticlesHandler
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ArticleRepository
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router


fun initializeContext(): BeanDefinitionDsl = beans {
    useArticleRepository()
    articlesRoutes()
}

fun BeanDefinitionDsl.useArticleRepository() {
    bean { ArticleRepository() }
}

fun BeanDefinitionDsl.articlesRoutes() {
    bean {
        router {
            "api".nest {
                val handler = ArticlesHandler(ref())

                GET("/articles", handler::find)
                GET("/articles/{id}", handler::find)
                DELETE("/articles/{id}", handler::remove)
                accept(MediaType.APPLICATION_JSON).nest {
                    POST("/articles", handler::new)
                    PUT("/articles/{id}", handler::update)
                }
            }
        }
    }
}