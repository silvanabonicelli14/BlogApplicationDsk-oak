package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.inbound.routes.ArticlesHandler
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.InMemoryArticlesRepository
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.exposed.ExposedArticlesRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.integration.spring.SpringLiquibase
import org.jetbrains.exposed.sql.Database
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.core.env.get
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router
import javax.sql.DataSource

fun initializeContext(): BeanDefinitionDsl = beans {
    articlesRoutes()
    connectToH2FromEnv()

    env["app.liquibase.change-log"]
        ?.run(::enableLiquibase)
        ?: println("Mi attendevo la property app.liquibase.change-log per inizializzare lo schema, ma non c'e'")

}

fun BeanDefinitionDsl.enableLiquibase (filepath: String) {
    bean {
        SpringLiquibase().apply{
            changeLog = filepath
            dataSource = ref()
        }
    }
}

fun BeanDefinitionDsl.connectToDb(connectionString: String, driver: String, username: String?, password: String?) {
    val config = HikariConfig().apply {
        jdbcUrl = connectionString
        driverClassName = driver
        maximumPoolSize = 10
        username?.let { this.username = username }
        password?.let { this.password = password }
    }
    val datasource = HikariDataSource(config)
    Database.connect(datasource)

    bean { ExposedArticlesRepository() }
    bean<DataSource> { datasource }
}

fun BeanDefinitionDsl.connectToH2FromEnv() {
    connectToDb(
        env["blogapplicationdsl.connectionstring"]!!,
        env["blogapplicationdsl.driver"]!!,
        env["blogapplicationdsl.username"],
        env["blogapplicationdsl.password"]
    )
}

fun BeanDefinitionDsl.useInmemoryRepository() {
    bean { InMemoryArticlesRepository() }
}

fun BeanDefinitionDsl.articlesRoutes() {
    bean {
        router {
            "api".nest {
                val handler = ArticlesHandler(ref())

                GET("/articles", handler::find)
                GET("/articles/{id}", handler::find)

                accept(MediaType.APPLICATION_JSON).nest {
                    POST("/articles", handler::save)
                }
            }
        }
    }
}
