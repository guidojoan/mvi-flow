package com.astutify.mvi.demo.presentation.screen.ingredients

import com.astutify.mvi.demo.presentation.model.IngredientViewModel


sealed class IngredientsViewEvent {
    object Loading : IngredientsViewEvent()
    object LoadData : IngredientsViewEvent()
    object LoadingError : IngredientsViewEvent()
    object SearchError : IngredientsViewEvent()
    class Search(val name: String) : IngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
    class IngredientClicked(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickAddIngredient : IngredientsViewEvent()
    class IngredientAdded(val ingredient: IngredientViewModel) : IngredientsViewEvent()
    object ClickRefresh : IngredientsViewEvent()
    object ClickCloseSearch : IngredientsViewEvent()
}