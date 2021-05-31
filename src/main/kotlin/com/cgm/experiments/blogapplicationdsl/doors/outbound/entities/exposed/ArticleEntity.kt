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
}

class ArticleDao(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<ArticleDao>(ArticleEntity)

    var title by ArticleEntity.title
    var body by ArticleEntity.body
}