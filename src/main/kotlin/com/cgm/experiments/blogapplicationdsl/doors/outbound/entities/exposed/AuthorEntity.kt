package com.cgm.experiments.blogapplicationdsl.doors.outbound.entities.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object AuthorEntity : IntIdTable("blog.authors") {
    val name: Column<String> = varchar("name", 50)
}

class AuthorDao(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<AuthorDao>(AuthorEntity)

    var name by AuthorEntity.name
}