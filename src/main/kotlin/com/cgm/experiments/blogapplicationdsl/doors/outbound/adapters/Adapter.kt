package com.cgm.experiments.blogapplicationdsl.doors.outbound.adapters

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.*

class Adapter {
    companion object AdapterFactory {
        fun articleAdapter(article: Article): ArticleDto =
            ArticleDto(
                article.id.toString(),
                "articles",
                ArticleDtoAttributes(
                    article.title,
                    article.body,
                    Author(
                        article.author.id.toString(), "authors",
                        AuthorAttributes(article.author.name)
                    )
                ),
                ArticleDtoRelationships(
                    ArticleDtoRelationshipsComments(article.comments?.map {
                        ArticleDtoRelationshipsCommentsData(
                            it.id.toString(),
                            it.comment
                        )
                    })
                )
            )

//        fun articleDtoAdapter(article: Article): ArticleDtoManual {
//            val listOfComments = article.comments?.map { Comment(it.id, it.comment) }
//            return ArticleDtoManual(
//                article.id,
//                "articles",
//                Attributes(
//                    article.title,
//                    article.body,
//                    listOfComments,
//                    AttributesAuthor(article.author.id, article.author.name)
//                )
//            )
//        }

        fun articlesDtoAdapter(article: List<Article>): List<ArticleDto> {

            return article.map {
                ArticleDto(
                    it.id.toString(),
                    "articles",
                    ArticleDtoAttributes(
                        it.title,
                        it.body,
                        com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.Author(
                            it.author.id.toString(),
                            "authors",
                            AuthorAttributes(it.author.name)
                        )
                    ),
                    ArticleDtoRelationships(ArticleDtoRelationshipsComments(it.comments?.map { comm ->
                        ArticleDtoRelationshipsCommentsData(
                            comm.id.toString(),
                            "articlecomments"
                        )
                    }))
                )
            }
        }

        fun articleDtoAdapter(article: Article): ArticleDto {

            return ArticleDto(
                    article.id.toString(),
                    "articles",
                    ArticleDtoAttributes(
                        article.title,
                        article.body,
                        com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.Author(
                            article.author.id.toString(),
                            "authors",
                            AuthorAttributes(article.author.name)
                        )
                    ),
                    ArticleDtoRelationships(ArticleDtoRelationshipsComments(article.comments?.map { comm ->
                        ArticleDtoRelationshipsCommentsData(
                            comm.id.toString(),
                            "articlecomments"
                        )
                    }))
                )
        }


//
//        fun articleDtoForInsertAdapter(article: ArticleDtoManual): Article {
//            return Article(
//                article.id,
//                article.attributes.title,
//                article.attributes.body,
//                article.attributes.comments.map { ArticleComment(it.id, it.comment,article.id) },
//                Author(article.attributes.author.id, article.attributes.author.name)
//                )
//        }

        fun articleDtoForInsertAdapter(article: ArticleDto): Article {
            return Article(
                article.id.toInt(),
                article.attributes.title,
                article.attributes.body,
                article.relationships.comments?.data?.map { ArticleComment(it.id?.toInt()!!, it.type!!,article.id.toInt()) },
                Author(article.attributes.author.id.toInt(), article.attributes.author.attributes.name)
            )
        }

    }
}