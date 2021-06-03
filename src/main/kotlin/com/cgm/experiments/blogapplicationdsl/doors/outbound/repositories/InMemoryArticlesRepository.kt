package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleCommentEntity
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.AuthorEntity
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class InMemoryArticlesRepository(initialValue: List<Article> = emptyList()): Repository<Article> {

    private val articles = initialValue.toMutableList()

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