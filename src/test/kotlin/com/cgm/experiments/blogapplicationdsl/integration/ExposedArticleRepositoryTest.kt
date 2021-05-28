package com.cgm.experiments.blogapplicationdsl.integration

import com.cgm.experiments.blogapplicationdsl.*
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleDao
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ExposedArticleRepository
import com.cgm.experiments.blogapplicationdsl.helpers.TestHelpers
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedArticleRepositoryTest {
    private lateinit var app: ConfigurableApplicationContext
    private val initialArticles = TestHelpers.articles

    @BeforeAll
    internal fun setUp() {
        app = start(RandomServerPort){
            beans {
                //connectToH2FromEnv()
                enableLiquibase()
                connectToPostgreFromEnv()
            }
        }
//        transaction {
//            SchemaUtils.create(ArticleEntity)
//        }
    }

    @BeforeEach
    internal fun beforeEach() {
        ExposedArticleRepository().reset()
    }

    @AfterAll
    internal fun tearDown() {
        app.close()
    }

    @Test
    fun `test getAll from repo`() {
        withExpected { expectedArticles ->
            ExposedArticleRepository().getAll() shouldBe expectedArticles
        }
    }

    @Test
    fun `can create a new article`() {
        ExposedArticleRepository().new(Article(1,"myArticle","contentArticle"))
    }

    private fun withExpected(test: (articles: List<Article>) -> Unit): Unit{
        transaction {
            initialArticles.map { ArticleDao.new {
                title = it.title
                body = it.body
            } }
                .map { Article(it.id.value, it.title, it.body) }
                .run(test)
        }
    }
}