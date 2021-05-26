package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.model.Article

class ArticleRepository{
     private val articles = mutableListOf(
         Article(1, "article x", "body article x"),
         Article(2, "article y", "body article y")
     )

    fun getAll() = articles

    fun getOne(id: Int) = articles.firstOrNull { it.id == id }
    fun new(article: Article): Article {
        val maxId = articles.maxByOrNull { it.id }?.id ?: 0
        val newArticle = article.copy(id = maxId + 1)
        articles.add(newArticle)
        return newArticle
    }

    fun update(article: Article): Article? {
        return articles.find { it.id == article.id }
            ?.let {
                val index = articles.indexOf(it)
                articles[index] = article
                return it
            }
    }
}