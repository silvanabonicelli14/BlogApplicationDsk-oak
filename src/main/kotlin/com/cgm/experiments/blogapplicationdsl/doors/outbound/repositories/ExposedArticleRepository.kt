package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author
import com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed.*
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
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

        val id = ArticleEntity.insertAndGetId {
            it[id] = article.id
            it[title] = article.title
            it[body] = article.body
            it[author_id] = article.author.id
        }
        Article(id.value, article.title, article.body,article.comments,article.author)
    }

    fun newComment(commentArticle: ArticleComment): ArticleComment = transaction {
        val id = ArticleCommentEntity.insertAndGetId {
            it[id] = commentArticle.id
            it[comment] = commentArticle.comment
            it[article_id] = commentArticle.article
        }

        ArticleComment(id.value, commentArticle.comment, commentArticle.article)
    }

    fun newAuthor(author: Author): Author = transaction {
        val id = AuthorEntity.insertAndGetId {
            it[id] = author.id
            it[name] = author.name
        }
        Author(id.value,author.name)
    }


    override fun update(article: Article): Article? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Article? {
        TODO("Not yet implemented")
    }

    fun reset() = transaction {
        ArticleCommentEntity.deleteAll()
        ArticleEntity.deleteAll()
        AuthorEntity.deleteAll()
        Unit
    }

    private fun toArticle(art: ArticleDao) =
        Article(
            art.id.value,
            art.title,
            art.body,
            art.comments.map { ArticleComment(it.id.value, it.comment, art.id.value) },
            Author(art.author.id.value,art.author.name))

    private fun toArticleComment(comment: ArticlesCommentDao) =
        ArticleComment(comment.id.value, comment.comment, comment.article.id.value)
}