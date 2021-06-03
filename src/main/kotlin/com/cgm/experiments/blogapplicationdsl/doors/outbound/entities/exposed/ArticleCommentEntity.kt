package com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ArticleCommentEntity : IntIdTable("Blog.articlecomments") {

    val comment: Column<String> = varchar("comment", 2000)
    val article_id = reference("article_id",ArticleEntity.id)
}

class ArticlesCommentDao(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<ArticlesCommentDao>(ArticleCommentEntity)

    var comment by ArticleCommentEntity.comment
    var article by ArticleDao referencedOn ArticleCommentEntity.article_id

}