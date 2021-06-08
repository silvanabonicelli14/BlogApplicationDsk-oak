package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles

import java.util.Objects
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.ArticleDtoAttributes
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.ArticleDtoRelationships
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
 * @param id 
 * @param type 
 * @param attributes 
 * @param relationships 
 */
data class ArticleDto(

    @field:JsonProperty("id", required = true) val id: kotlin.String,

    @field:JsonProperty("type", required = true) val type: kotlin.String,

    @field:Valid
    @field:JsonProperty("attributes", required = true) val attributes: ArticleDtoAttributes,

    @field:Valid
    @field:JsonProperty("relationships", required = true) val relationships: ArticleDtoRelationships
) {

}

