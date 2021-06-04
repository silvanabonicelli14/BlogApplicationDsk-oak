package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles

import java.util.Objects
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.ArticleAttributes
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.ArticleRelationships
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

    @field:JsonProperty("id") val id: kotlin.String? = null,

    @field:JsonProperty("type") val type: kotlin.String? = null,

    @field:Valid
    @field:JsonProperty("attributes") val attributes: ArticleAttributes? = null,

    @field:Valid
        @field:JsonProperty("relationships") val relationships: ArticleRelationships? = null
) {

}

