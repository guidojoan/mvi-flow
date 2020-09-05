package com.astutify.mvi.demo.presentation.screen.ingredients

import android.os.Parcelable
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientsViewState(
    val ingredients: List<IngredientViewModel> = emptyList(),
    val status: Status? = null
) : Parcelable {

    enum class Status {
        LOADING, LOADING_ERROR, NO_RESULTS, SEARCH_ERROR
    }

    fun copyState(
        ingredients: List<IngredientViewModel> = this.ingredients,
        status: Status? = null
    ) = copy(ingredients = ingredients, status = status)

    fun copyWithIngredient(ingredient: IngredientViewModel): IngredientsViewState {
        val shouldUpdate = ingredients.any { it.id == ingredient.id }
        return if (shouldUpdate) {
            copyState(ingredients = updateIngredient(ingredients, ingredient))
        } else {
            copyState(ingredients = addIngredient(ingredients, ingredient))
        }
    }

    private fun updateIngredient(
        ingredients: List<IngredientViewModel>,
        ingredient: IngredientViewModel
    ): List<IngredientViewModel> {
        return ingredients.map {
            if (it.id == ingredient.id) {
                ingredient
            } else {
                it
            }
        }
    }

    private fun addIngredient(
        ingredients: List<IngredientViewModel>,
        ingredient: IngredientViewModel
    ): List<IngredientViewModel> {
        return mutableListOf(ingredient).apply { addAll(ingredients) }
    }
}