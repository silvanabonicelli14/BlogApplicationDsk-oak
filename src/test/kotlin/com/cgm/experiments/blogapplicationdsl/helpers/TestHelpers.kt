package com.cgm.experiments.blogapplicationdsl.helpers

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment

object TestHelpers{
    val initialComments = listOf<ArticleComment>(
        ArticleComment(1,  "comment of the article x", 1),
        ArticleComment(2, "comment of the article x", 1)
    )

    val articles = listOf(
        Article(1, "title x", "body of the article x", initialComments),
        Article(2, "title y", "body of the article y",mutableListOf<ArticleComment>()))

}