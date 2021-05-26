package com.cgm.experiments.blogapplicationdsl

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.*
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlogApplicationDslApplicationTests {

    private lateinit var app: ConfigurableApplicationContext
    private lateinit var client: MockMvc
    private val mapper = jacksonObjectMapper()

    private val expectedArticles = listOf(
        Article(1,"article x", "body article x"),
        Article(2,"article y", "body article y"))

    @BeforeAll
    internal fun setUp() {
        app = start()
        client = MockMvcBuilders
            .webAppContextSetup(app as WebApplicationContext)
            .build()
    }

    @AfterAll
    internal fun tearDown() {
        app.close()
    }

    @Test
    fun `can read all articles`() {

        client.get("/api/articles")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(expectedArticles)) }
            }
    }

    @Test
    fun `can read one article`() {
        val id = 2
        val expectedArticle = expectedArticles.first { it.id == id }

        client.get("/api/articles/$id")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(expectedArticle)) }
            }
    }

    @Test
    fun `if the article do not exist return not found`() {
        client.get("/api/articles/9999999")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `if the article id is not a number it returns bad request`() {
        client.get("/api/articles/badRequestId")
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `can create a new article`() {
        val expectedArticle = Article(0, "article z", "body of article z")

        val articleStr = client.post("/api/articles"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(expectedArticle)
        }
        .andExpect {
            status { isCreated() }
        }.andReturn()
        .let {
            val article: Article = mapper.readValue(it.response.contentAsString)
            val location: String = it.response.getHeaderValue("location") as String
            Assertions.assertTrue(location == "http://localhost/api/articles/${article.id}")
            client.get("/api/articles/${article.id}")
                .andExpect {
                    status { isOk() }
                    content { json(mapper.writeValueAsString(article)) }
                }
        }
    }

    @Test
    fun `can modify an article`() {
        val modifiedArticle = Article(1, "MODIFIED article x", "body article x")
        val articleStr = client.put("/api/articles/1"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(modifiedArticle)
        }
        .andExpect {
            status { isOk() }
        }
        .andReturn().response.contentAsString

        val actualArticle = mapper.readValue<Article>(articleStr)
        client.get("/api/articles/${actualArticle.id}")
            .andExpect {
                status { isOk() }
                content { json(mapper.writeValueAsString(modifiedArticle)) }
            }
    }

    @Test
    fun `cannot modify an article because not found`() {
        val modifiedArticle = Article(9999, "MODIFIED article x", "body article x")
        client.put("/api/articles/9999"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(modifiedArticle)
        }
        .andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `can delete an article`() {
        client.delete("/api/articles/1")
            .andExpect {
                status { isOk() }
            }

        client.get("/api/articles/1")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `can't delete an article because does not exists`() {
        client.delete("/api/articles/9999")
            .andExpect {
                status { isNotFound() }
            }
    }
}