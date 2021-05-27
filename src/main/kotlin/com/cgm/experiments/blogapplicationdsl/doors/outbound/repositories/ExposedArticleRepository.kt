package com.cgm.experiments.blogapplicationdsl.doors.outbound.repositories

import com.cgm.experiments.blogapplicationdsl.domain.Repository
import com.cgm.experiments.blogapplicationdsl.domain.model.Article

class ExposedArticleRepository: Repository<Article> {
    override fun getAll(): MutableList<Article> {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Int): Article? {
        TODO("Not yet implemented")
    }

    override fun new(article: Article): Article {
        TODO("Not yet implemented")
    }

    override fun update(article: Article): Article? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Article? {
        TODO("Not yet implemented")
    }
}