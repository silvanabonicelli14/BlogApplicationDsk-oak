package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers.ArticlesHandler
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ExposedArticleRepository
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.InMemoryArticlesRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.integration.spring.SpringLiquibase
import org.jetbrains.exposed.sql.Database
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.core.env.get
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource


fun initializeContext(): BeanDefinitionDsl = beans {
    useArticleRepository()
    connectToH2FromEnv()
    articlesRoutes()
    enableLiquibase(env["app.liquibase.change-log"]!!)

//    env["app.liquibase.change-log"]
//        ?.run(::enableLiquibase)
//        ?://TODO "controllo parametro vuoto-> loggo errore o eccezione loggata"

    //useRepository()
}

fun BeanDefinitionDsl.connectToH2FromEnv() {
    connectToDb(
        env["h2.datasource.url"]!!,
        env["h2.datasource.driverClassName"]!!,
        env["h2.datasource.username"]!!,
        env["h2.datasource.password"]!!
    )
}

fun BeanDefinitionDsl.connectToPostgreFromEnv(postgreSQLContainer: PostgreSQLContainer<Nothing>) {
    connectToDb(
        postgreSQLContainer.jdbcUrl,
        "org.postgresql.Driver",
        postgreSQLContainer.username,
        postgreSQLContainer.password
    )
}

//fun BeanDefinitionDsl.connectToPostgreFromEnv() {
//    connectToDb(
//        env["postgre.datasource.url"]!!,
//        env["postgre.datasource.driverClassName"]!!,
//        env["postgre.datasource.username"]!!,
//        env["postgre.datasource.password"]!!
//    )
//}

fun BeanDefinitionDsl.connectToDb(connectionString: String, driver: String, username: String, password: String) {
    val config = HikariConfig().apply {
        jdbcUrl = connectionString
        driverClassName = driver
        maximumPoolSize = 10
        this.username = username
        this.password = password
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    bean <DataSource> { dataSource }
}

fun BeanDefinitionDsl.enableLiquibase (filepath: String ) {
    bean {
        SpringLiquibase().apply{
            changeLog = filepath
            dataSource = ref()
        }
    }
}

fun BeanDefinitionDsl.useRepository () {
    bean { ExposedArticleRepository() }
}

fun BeanDefinitionDsl.useArticleRepository() {
    bean { InMemoryArticlesRepository() }
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