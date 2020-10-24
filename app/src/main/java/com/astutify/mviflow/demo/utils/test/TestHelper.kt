package com.astutify.mviflow.demo.utils.test

import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel

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
