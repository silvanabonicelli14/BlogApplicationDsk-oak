package com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles

import java.util.Objects
import com.cgm.experiments.blogapplicationdsl.doors.outbound.dtos.articles.AuthorAttributes
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
 */
data class Author(

    @field:JsonProperty("id") val id: kotlin.String,

    @field:JsonProperty("type") val type: kotlin.String,

    @field:Valid
    @field:JsonProperty("attributes") val attributes: AuthorAttributes
) {

}

