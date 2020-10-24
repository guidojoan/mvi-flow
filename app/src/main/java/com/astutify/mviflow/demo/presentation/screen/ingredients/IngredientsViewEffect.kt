package com.astutify.mviflow.demo.presentation.screen.ingredients

import com.astutify.mviflow.demo.presentation.model.IngredientViewModel

sealed class IngredientsViewEffect {
    object LoadData : IngredientsViewEffect()
    class Search(val name: String) : IngredientsViewEffect()
    object GoToAddIngredient : IngredientsViewEffect()
    class GoToEditIngredient(val ingredient: IngredientViewModel) : IngredientsViewEffect()
}
