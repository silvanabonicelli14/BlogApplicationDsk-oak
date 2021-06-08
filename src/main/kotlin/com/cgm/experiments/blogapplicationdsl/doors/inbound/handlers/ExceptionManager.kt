package com.cgm.experiments.blogapplicationdsl.doors.inbound.handlers

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

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

class ArticleNotFoundException(id: String) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found ($id)")

