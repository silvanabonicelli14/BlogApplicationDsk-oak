package com.cgm.experiments.blogapplicationdsl.integration

import com.cgm.experiments.blogapplicationdsl.connectToH2FromEnv
import com.cgm.experiments.blogapplicationdsl.connectToPostgreFromEnv
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleDao
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticlesCommentDao
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ExposedArticleRepository
import com.cgm.experiments.blogapplicationdsl.enableLiquibase
import com.cgm.experiments.blogapplicationdsl.helpers.MyPostgresContainer
import com.cgm.experiments.blogapplicationdsl.helpers.TestHelpers
import com.cgm.experiments.blogapplicationdsl.start
import com.cgm.experiments.blogapplicationdsl.utils.RandomServerPort
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class ExposedArticleRepositoryTest {
    private lateinit var app: ConfigurableApplicationContext
    private val initialArticles = TestHelpers.articles
    private val initialComments = TestHelpers.initialComments
    private val initialAuthors = TestHelpers.authors

    companion object{
        @Container
        val container = MyPostgresContainer.container
    }

    @BeforeAll
    internal fun setUp() {
        app = start(RandomServerPort){
            beans {
                //connectToH2FromEnv()
                //connectToPostgreFromEnv(container)
                connectToPostgreFromEnv()
                enableLiquibase("classpath:/liquibase/db-changelog-master.xml")
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
    fun `can create a new article`() {
      transaction {
        ExposedArticleRepository().reset()
        ExposedArticleRepository().new(Article(1,"myArticle","contentArticle", listOf<ArticleComment>(), Author(1, "Author")))
        }
    }

    @Test
    fun `test getAll from repo`() {
        withExpected { expectedArticles ->
            ExposedArticleRepository().getAll() shouldBe expectedArticles
        }
    }

    @Test
    fun `can get one article and his comments`() = withExpected { expectedArticles ->
        val expected = expectedArticles.first()
        ExposedArticleRepository().getOne(expected.id) shouldBe expected
    }

    private fun withExpected(test: (articles: List<Article>) -> Unit): Unit{
        transaction{
            initialAuthors.map(ExposedArticleRepository()::newAuthor)
            initialArticles
                .map(ExposedArticleRepository()::new)

            initialComments.map(ExposedArticleRepository()::newComment)
        }
        initialArticles.let(test)
    }
}