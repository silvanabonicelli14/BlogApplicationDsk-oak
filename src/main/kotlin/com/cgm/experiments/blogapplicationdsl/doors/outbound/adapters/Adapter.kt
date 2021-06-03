package com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.*

class Adapter {
    companion object AdapterFactory {
        fun articleAdapter(article: Article): ArticleDto =
            ArticleDto(article.id, article.title, article.body, article.comments, article.author)

        fun articleDtoAdapter(article: Article): ArticleDtoManual {
            val listOfComments = article.comments.map { Comment(it.id, it.comment) }
            return ArticleDtoManual(
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

        fun articleDtoForInsertAdapter(article: ArticleDtoManual): Article {
            return Article(
                article.id,
                article.attributes.title,
                article.attributes.body,
                article.attributes.comments.map { ArticleComment(it.id, it.comment,article.id) },
                Author(article.attributes.author.id, article.attributes.author.name)
                )
        }

//        fun articleDtoForInsertAdapter(article: ArticleForInsertDto): Article {
//            return Article(
//                article.id,
//                article.attributes.title,
//                article.attributes.body,
//                article.relationships.comments.data.map { ArticleComment(it.id, it.type,article.id) },
//                Author(article.relationships.author.data.id, article.relationships.author.data.t)
//            )
//        }

    }
}