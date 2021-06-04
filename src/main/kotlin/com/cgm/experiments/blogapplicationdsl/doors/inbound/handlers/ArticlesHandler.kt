package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters.Adapter
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.ArticleDtoManual
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.ArticleForInsertDto
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder.jsonApiModel
import com.toedter.spring.hateoas.jsonapi.MediaTypes
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI


class ArticlesHandler(private val repository: Repository<Article>) {

    private val logger: Logger = LogManager.getLogger(ArticlesHandler::class.java)
    @GetMapping("/{id}")
    fun find(request: ServerRequest): ServerResponse {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");

        return (request.inPath("id")
            ?.run(::getOne)
            ?: okArticoleDtoResponseAll(repository.getAll()))
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

    fun new(request: ServerRequest): ServerResponse {
        val body = request
            .body(ArticleForInsertDto::class.java)
        return body
            .let { article ->  repository.new(Adapter.articleDtoForInsertAdapter(article))}
            .let {
                    article -> ServerResponse.created(URI("${request.toUri()}/api/articles/${article.id}")).body(article)
            }
    }

    private fun getOne(id: String) = (id.toIntOrNull()?.let { intId ->
        repository.getOne(intId)
            ?.run(::okArticleDtoResponse)
            ?: ServerResponse.notFound().build()
    }
        ?: ServerResponse.badRequest().build())

    private fun okResponse(any: Any): ServerResponse = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(any)



    private fun okArticoleDtoResponseAll(articles: List<Article>): ServerResponse {
        val articlesList: List<RepresentationModel<*>> = articles.map { articleToRepresentationModel(it) }

        val pageMetadata = PagedModel.PageMetadata(10, 1, 100, 10)
        val pagedModel: PagedModel<RepresentationModel<*>> = PagedModel.of(articlesList, pageMetadata)

        return ServerResponse.ok()
            .contentType(MediaTypes.JSON_API)
            .body(
                jsonApiModel()
                    .model(pagedModel)
                    .pageMeta()
                    .build()
            )
    }

    private fun okArticleDtoResponse(article: Article): ServerResponse {
        return ServerResponse.ok()
            .contentType(MediaTypes.JSON_API)
            .body(
                articleToRepresentationModel(article)
                //Adapter.articleDtoAdapter(article)
            )
    }


    private fun articleToRepresentationModel(article: Article): RepresentationModel<*> {
        val articleDto = Adapter.articleAdapter(article)
        return jsonApiModel()
            .model(articleDto)
            .relationship("comments", articleDto.comments)
            .relationship("author", articleDto.author)
            .included(articleDto.comments)
            .included(articleDto.author)
            .build()
    }

    private fun ServerRequest.inPath(name: String): String? = try {
        pathVariable(name)
    }catch (ex: IllegalArgumentException){
        null
    }


    private fun ServerRequest.toUri(): String{
        return "${uri().scheme}://${uri().host}"
    }

    private fun delete(id: String) = (id.toIntOrNull()?.let {intId ->
        repository.delete(intId)
            ?.run(::okResponse)
            ?: ServerResponse.notFound().build()
        }
        ?: ServerResponse.badRequest().build())
}