package com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insertAndGetId

object ArticleEntity : IntIdTable("blog.articles") {

    val title: Column<String> = varchar("title", 50)
    val body: Column<String> = varchar("body", 2000)
    val author_id = reference("author_id", AuthorEntity.id)
}

class ArticleDao(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<ArticleDao>(ArticleEntity)

    var title by ArticleEntity.title
    var body by ArticleEntity.body
    val comments by ArticlesCommentDao referrersOn ArticleCommentEntity.article_id
    val author by AuthorDao referencedOn ArticleEntity.author_id
}

object ArticleCommentEntity : IntIdTable("Blog.articlecomments") {

    val comment: Column<String> = varchar("comment", 2000)
    val article_id = reference("article_id",ArticleEntity.id)
}

class ArticlesCommentDao(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<ArticlesCommentDao>(ArticleCommentEntity)

    var comment by ArticleCommentEntity.comment
    var article by ArticleDao referencedOn ArticleCommentEntity.article_id

}
