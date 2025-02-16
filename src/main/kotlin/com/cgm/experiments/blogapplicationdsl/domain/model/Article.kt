package com.cgm.experiments.blogapplicationdsl.domain.model
data class Article(
    val id: Int,
    val title: String,
    val body: String,
    val comments: List<ArticleComment>?,
    val author: Author
)