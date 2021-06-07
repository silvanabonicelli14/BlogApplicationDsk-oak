package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles

import java.util.Objects
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.Author
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid

/**
 * 
 * @param title 
 * @param body 
 * @param author 
 */
data class ArticleAttributes(

    @field:JsonProperty("title") val title: kotlin.String,

    @field:JsonProperty("body") val body: kotlin.String,

    @field:Valid
    @field:JsonProperty("author") val author: Author
) {

}

