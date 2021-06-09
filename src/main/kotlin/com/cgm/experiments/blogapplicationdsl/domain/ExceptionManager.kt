package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.CustomError
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException
import java.util.*


object ExceptionManager
{
    private val logger: Logger = LogManager.getLogger(ArticlesHandler::class.java)

    fun notFoundException(stack: String, entity: String) {
        logger.error("Handling of $stack  resulted in Exception ($entity not found)")
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "$entity not found");
    }

    fun myException(): ResponseStatusException {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, " not valid");
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Article not valid")
class ArticleNotValidException(id: String) : Exception(id)

class AuthorNotFoundException(message: String) :
    ResponseStatusException(HttpStatus.NOT_FOUND, message)

class ArticleNotFoundException(message: String) :
    ResponseStatusException(HttpStatus.BAD_REQUEST, message);




@Component
class CustomErrorResponse: DefaultErrorAttributes(){
    override fun getErrorAttributes(webRequest: WebRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(webRequest, options)
        val throwable = getError(webRequest)
        val errorBody = mutableMapOf<String, Any>()
        val error = CustomError(errorAttributes["status"].toString(),throwable.message?.substringAfter("\"")?.removeSuffix("\"").toString())
        errorBody["errors"]=  error
        return errorBody
    }
}