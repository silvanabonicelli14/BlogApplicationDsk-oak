package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos

data class ArticleForInsertDto (
    val id: Int,
    val type: String,
    val attributes: AttributesInsert,
    val relationships: Relationships
)

data class AttributesInsert (
    val title: String,
    val body: String
)

data class Relationships (
    val comments: Comments,
    val author: Author
)

data class Author (
    val data: Data
)

data class Data (
    val id: Int,
    val type: String
)

data class Comments (
    val data: List<Data>
)