package com.astutify.mvi.demo.presentation.screen.editingredient

sealed class EditIngredientViewEffect {
    object Save : EditIngredientViewEffect()
    object GoBack : EditIngredientViewEffect()
}