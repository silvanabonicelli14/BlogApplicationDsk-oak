package com.cgm.experiments.blogapplicationdsl.domain.model

data class ArticleComment (
    val id: Int,
    val comment: String,
    val article: Int
)