package com.cgm.experiments.blogapplicationdsl.helpers

import com.cgm.experiments.blogapplicationdsl.domain.model.Article
import com.cgm.experiments.blogapplicationdsl.domain.model.ArticleComment
import com.cgm.experiments.blogapplicationdsl.domain.model.Author

object TestHelpers{
    val authors = listOf(
        Author(1, "Author 1"),
        Author(2, "Author 2"))

    val initialComments = listOf<ArticleComment>(
        ArticleComment(1,  "comment of the article x", 1),
        ArticleComment(2, "comment of the article x", 1)
    )

    val articles = listOf(
        Article(1, "title x", "body of the article x", initialComments,authors[0]),
        Article(2, "title y", "body of the article y",mutableListOf<ArticleComment>(),authors[1]))



}