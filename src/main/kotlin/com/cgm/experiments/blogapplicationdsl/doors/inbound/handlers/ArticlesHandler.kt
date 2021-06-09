package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters.Adapter
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.ArticleDto
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder.jsonApiModel
import com.toedter.spring.hateoas.jsonapi.MediaTypes
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

class ArticlesHandler(private val repository: Repository<Article>) {

    private val logger: Logger = LogManager.getLogger(ArticlesHandler::class.java)
    fun find(request: ServerRequest): ServerResponse {
        logger.debug("Debug message")
        logger.info("Info message")
        logger.warn("Warn message")
        logger.error("Error message")

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

//    With DTOS manual

//    fun new(request: ServerRequest): ServerResponse {
//        val body = request
//            .body(ArticleForInsertDto::class.java)
//        return body
//            .let { article ->  repository.new(Adapter.articleDtoForInsertAdapter(article))}
//            .let {
//                    article -> ServerResponse.created(URI("${request.toUri()}/api/articles/${article.id}")).body(article)
//            }
//    }

    // With dtos generate by OPENAPITOOLS library
    fun new(request: ServerRequest): ServerResponse {
        val body = request
            .body(ArticleDto::class.java)
        return body
            .let { article ->
                repository.new(Adapter.articleDtoForInsertAdapter(article))
                ServerResponse.created(URI("${request.toUri()}/api/articles/${article.id}")).body(article)
            }
    }

    private fun getOne(id: String) = (id.toIntOrNull()?.let { intId ->
        repository.getOne(intId)
            ?.run(::okArticleDtoResponse)
            ?: throw ArticleNotFoundException("Article not found ($id)")
    }
        ?: throw ArticleNotValidException(id))

    private fun getOneWithLog(id: String) =
        try {
            repository.getOne(id.toInt())
                ?.run(::okArticleDtoResponse)
                ?: throw ArticleNotFoundException(id)
        } catch (exc: Exception) {
            logger.error("Handling of [ ${exc.javaClass.name} ] resulted in Exception (${exc.message})")
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Article Id not valid"
            )
        }

    private fun okResponse(any: Any): ServerResponse = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(any)

//    With manual and spring-hateoas-jsonapi library
//
//    private fun okArticoleDtoResponseAll(articles: List<Article>): ServerResponse {
//        val articlesList: List<RepresentationModel<*>> = articles.map { articleToRepresentationModel(it) }
//
//        val pageMetadata = PagedModel.PageMetadata(10, 1, 100, 10)
//        val pagedModel: PagedModel<RepresentationModel<*>> = PagedModel.of(articlesList, pageMetadata)
//
//        return ServerResponse.ok()
//            .contentType(MediaTypes.JSON_API)
//            .body(
//                jsonApiModel()
//                    .model(pagedModel)
//                    .pageMeta()
//                    .build()
//            )
//    }

    private fun okArticoleDtoResponseAll(articles: List<Article>): ServerResponse {
        return ServerResponse.ok()
            .contentType(MediaTypes.JSON_API)
            .body(
                Adapter.articlesDtoAdapter(articles)
            )
    }

//    private fun okArticleDtoResponse(article: Article): ServerResponse {
//        return ServerResponse.ok()
//            .contentType(MediaTypes.JSON_API)
//            .body(
//                articleToRepresentationModel(article)
//                //Adapter.articleDtoAdapter(article)
//            )
//    }

    private fun okArticleDtoResponse(article: Article): ServerResponse {
        return ServerResponse.ok()
            .contentType(MediaTypes.JSON_API)
            .body(Adapter.articleDtoAdapter(article))
    }


    private fun articleToRepresentationModel(article: Article): RepresentationModel<*> {
        val articleDto = Adapter.articleAdapter(article)
        return jsonApiModel()
            .model(articleDto)
            .relationship("comments", articleDto.relationships.comments?.data!!)
            .relationship("author", articleDto.attributes.author)
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