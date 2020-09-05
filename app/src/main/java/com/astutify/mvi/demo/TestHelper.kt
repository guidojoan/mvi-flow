package com.astutify.mvi.demo

import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.presentation.model.IngredientViewModel

class TestHelper {
    companion object {

        fun getIngredientVM() =
            IngredientViewModel(
                id = "ingredientId",
                name = "Tomato"
            )

        fun getIngredient() =
            Ingredient(
                id = "ingredientId",
                name = "Tomato"
            )
    }
}
