package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos

import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.fasterxml.jackson.annotation.JsonIgnore

data class ArticleDto(
    val id: Int,
    val title: String,
    val body: String,
    @JsonIgnore
    val comments: List<ArticleComment>,
    @JsonIgnore
    val author: Author
)