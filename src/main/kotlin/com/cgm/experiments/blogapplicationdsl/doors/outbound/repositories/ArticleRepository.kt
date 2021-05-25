package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.model.Article

class ArticleRepository{
     val articles = listOf(
         Article(1, "article x", "body article x"),
         Article(2, "article y", "body article y")
     )

    fun getAll() = articles

    fun getOne(id: Int) = articles.firstOrNull { it.id == id }
    fun save(article: Article): Article {
        return article
    }
}