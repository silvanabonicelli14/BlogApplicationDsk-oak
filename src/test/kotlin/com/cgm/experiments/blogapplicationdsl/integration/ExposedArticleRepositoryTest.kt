package com.cgm.experiments.blogapplicationdsl.integration

import com.cgm.experiments.blogapplicationdsl.RandomServerPort
import com.cgm.experiments.blogapplicationdsl.connectToH2FromEnv
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.ArticleDao
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.ArticleEntity
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ExposedArticleRepository
import com.cgm.experiments.blogapplicationdsl.enableLiquibase
import com.cgm.experiments.blogapplicationdsl.helpers.TestHelpers
import com.cgm.experiments.blogapplicationdsl.start
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.beans

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposedArticleRepositoryTest {
    private lateinit var app: ConfigurableApplicationContext
    private val initialArticles = TestHelpers.articles

    @BeforeAll
    internal fun setUp() {
        app = start(RandomServerPort){
            beans {
                connectToH2FromEnv()
                enableLiquibase()
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