package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

object ArticlesHandler{
    private val articleRepository  = ArticleRepository()

    fun find(request: ServerRequest): ServerResponse = (request.inPath("id")
        ?.run(::getOne)
        ?: okResponse(articleRepository.getAll()))

    private fun getOne(id: String) = (id.toIntOrNull()?.let { intId ->
        articleRepository.getOne(intId)
            ?.run(::okResponse)
            ?: ServerResponse.notFound().build()
        }
        ?: ServerResponse.badRequest().build())

    private fun okResponse(any: Any): ServerResponse = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(any)

    private fun ServerRequest.inPath(name: String): String? = try {
        pathVariable(name)
    }catch (ex: IllegalArgumentException){
        null
    }

    fun new(request: ServerRequest): ServerResponse {
        val body = request
            .body(Article::class.java)
        return body
            .let { article ->  articleRepository.new(article)}
            .let {
                article -> ServerResponse.created(URI("${request.toUri()}/api/articles/${article.id}")).body(article)
            }
    }
    private fun ServerRequest.toUri(): String{
        return "${uri().scheme}://${uri().host}"
    }
    fun update(request: ServerRequest): ServerResponse {
        val body = request
            .body(Article::class.java)
        return articleRepository.update(body)
            ?.run(::okResponse)
            ?:ServerResponse.notFound().build()
    }

    fun remove(request: ServerRequest): ServerResponse = (request.inPath("id")
        ?.run(::delete)
        ?: ServerResponse.notFound().build())

    private fun delete(id: String) = (id.toIntOrNull()?.let {intId ->
        articleRepository.delete(intId)
            ?.run(::okResponse)
            ?: ServerResponse.notFound().build()
        }
        ?: ServerResponse.badRequest().build())
}