package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos

import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.fasterxml.jackson.annotation.JsonIgnore

data class ArticleDto(
    val id: Int,
    val title: String,
    val body: String,
    val comments: List<ArticleComment>,
    val author: Author
)

data class ArticleDto1 (
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

data class Relationships (
    val comments: Comments,
    val author: RelationshipsAuthor
)

data class RelationshipsAuthor (
    val data: DAT
)

data class DAT (
    val id: String,
    val type: String
)

data class Comments (
    val data: List<DAT>
)

data class Meta (
    val page: Page
)

data class Page (
    val number: Long,
    val size: Long,
    val totalPages: Long,
    val totalElements: Long
)
