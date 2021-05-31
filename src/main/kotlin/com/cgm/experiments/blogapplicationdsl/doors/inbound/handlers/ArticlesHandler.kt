package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import com.cgm.experiments.blogapplicationdsl.BlogApplicationDslApplication
import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

class ArticlesHandler(private val repository: Repository<Article>){

    private val logger: Logger = LogManager.getLogger(ArticlesHandler::class.java)

    fun find(request: ServerRequest): ServerResponse {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");

        return (request.inPath("id")
            ?.run(::getOne)
            ?: okResponse(repository.getAll()))
    }

    private fun getOne(id: String) = (id.toIntOrNull()?.let { intId ->
        repository.getOne(intId)
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
            .let { article ->  repository.new(article)}
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
        return repository.update(body)
            ?.run(::okResponse)
            ?:ServerResponse.notFound().build()
    }

    fun remove(request: ServerRequest): ServerResponse = (request.inPath("id")
        ?.run(::delete)
        ?: ServerResponse.notFound().build())

    private fun delete(id: String) = (id.toIntOrNull()?.let {intId ->
        repository.delete(intId)
            ?.run(::okResponse)
            ?: ServerResponse.notFound().build()
        }
        ?: ServerResponse.badRequest().build())
}