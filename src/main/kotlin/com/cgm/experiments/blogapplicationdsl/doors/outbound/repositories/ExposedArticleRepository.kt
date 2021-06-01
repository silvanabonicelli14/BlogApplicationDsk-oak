package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleCommentEntity
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleDao
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticleEntity
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.ArticlesCommentDao
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedArticleRepository: Repository<Article> {
    override fun getAll(): MutableList<Article> = transaction {
        ArticleDao.all()
            .map(::toArticle)
    } as MutableList<Article>

    override fun getOne(id: Int): Article? = transaction {
        ArticleDao
            .findById(id)
            ?.let(::toArticle)
    }

    override fun new(article: Article): Article = transaction {
        ArticleDao.new {
            title = article.title
            body = article.body
        }.let(::toArticle)
    }

    fun newComment(commentArticle: ArticleComment): ArticleComment = transaction {
        ArticleCommentEntity.insert {
            it[comment] = commentArticle.comment
            it[article_id] = commentArticle.article
        }

        ArticleComment(commentArticle.id, commentArticle.comment, commentArticle.article)
    }


    override fun update(article: Article): Article? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Article? {
        TODO("Not yet implemented")
    }

    fun reset() = transaction {
        ArticleEntity.deleteAll()
        ArticleCommentEntity.deleteAll()
        Unit
    }

    private fun toArticle(art: ArticleDao) =
        Article(art.id.value, art.title, art.body, art.comments.map { ArticleComment(it.id.value,it.comment,art.id.value) })

    private fun toArticleComment(comment: ArticlesCommentDao) =
        ArticleComment(comment.id.value, comment.comment, comment.article.id.value)
}