package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos

import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.fasterxml.jackson.annotation.JsonIgnore
import com.toedter.spring.hateoas.jsonapi.JsonApiRelationships
import com.toedter.spring.hateoas.jsonapi.JsonApiType

data class ArticleDto(
    val id: Int,
    val title: String,
    val body: String,
    val comments: List<ArticleCommentDto>,
    val author: AuthorDto
)

data class ArticleCommentDto (
    val id: Int,
    val comment: String,
    val article: Int
)


data class AuthorDto (
    val id: Int,
    val name: String)

data class ArticleDtoManual (
    val id: Int,
    val type: String,
    val attributes: Attributes
)

data class Attributes (
    val title: String,
    val body: String,
    val comments: List<Comment>,
    val author: AttributesAuthor
)

data class AttributesAuthor (
    val id: Int,
    val name: String
)

data class Comment (
    val id: Int,
    val comment: String
)