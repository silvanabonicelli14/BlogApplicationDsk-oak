package com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.*

class Adapter {
    companion object AdapterFactory {
        fun articleAdapter(article: Article): ArticleDto =
            ArticleDto(article.id, article.title, article.body, article.comments, article.author)

        fun articleDtoAdapter(article: Article): ArticleDto1 {
            val listOfComments = article.comments.map { Comment(it.id, it.comment) }
            return ArticleDto1(
                article.id,
                "articles",
                Attributes(
                    article.title,
                    article.body,
                    listOfComments,
                    AttributesAuthor(article.author.id, article.author.name)
                )
            )
        }

    }
}