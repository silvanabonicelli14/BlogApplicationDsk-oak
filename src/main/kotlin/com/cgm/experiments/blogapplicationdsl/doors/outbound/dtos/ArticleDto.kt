package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos

data class ArticleDto2(//for doing jsonApi throw spring-hateoas-jsonapi library
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




data class ArticleDtoManual (//for doing jsonApi manually
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