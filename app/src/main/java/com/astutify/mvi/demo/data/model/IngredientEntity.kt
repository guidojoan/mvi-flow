package com.astutify.mvi.demo.data.model

import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.domain.model.ToDomain
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientEntity(
    val id: String,
    val name: String
) : ToDomain<Ingredient> {

    override fun toDomain() = Ingredient(id, name)
}
