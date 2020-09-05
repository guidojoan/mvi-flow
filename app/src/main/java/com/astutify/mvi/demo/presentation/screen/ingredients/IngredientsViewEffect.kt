package com.astutify.mvi.demo.presentation.screen.ingredients

import com.astutify.mvi.demo.presentation.model.IngredientViewModel

sealed class IngredientsViewEffect {
    object LoadData : IngredientsViewEffect()
    class Search(val name: String) : IngredientsViewEffect()
    object GoToAddIngredient : IngredientsViewEffect()
    class GoToEditIngredient(val ingredient: IngredientViewModel) : IngredientsViewEffect()
}
