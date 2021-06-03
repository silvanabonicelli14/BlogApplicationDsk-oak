package com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.ArticleDto

class Adapter {
    companion object AdapterFactory {
        fun articleAdapter(article: Article): ArticleDto =
            ArticleDto(article.id, article.title, article.body, article.comments, article.author)
    }
}