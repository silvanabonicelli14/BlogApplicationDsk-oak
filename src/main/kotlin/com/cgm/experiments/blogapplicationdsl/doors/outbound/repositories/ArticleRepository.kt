package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article

class ArticleRepository : Repository<Article> {
     private val articles = mutableListOf(
         Article(1, "article x", "body article x"),
         Article(2, "article y", "body article y")
     )

    override fun getAll() = articles

    override fun getOne(id: Int) = articles.firstOrNull { it.id == id }

    override fun new(article: Article): Article {
        val maxId = articles.maxByOrNull { it.id }?.id ?: 0
        val newArticle = article.copy(id = maxId + 1)
        articles.add(newArticle)
        return newArticle
    }

    override fun update(article: Article): Article? {
        return getOne(article.id)
            ?.let {
                articles[articles.indexOf(it)] = article
                it
            }
    }

    override fun delete(id: Int) = run {
        getOne(id)
            ?.let {
                articles.removeAt(articles.indexOf(it))
            }
    }

    fun reset(initialValue: List<Article> = emptyList()) = articles.clear().apply {
        articles.addAll(initialValue)
    }
}