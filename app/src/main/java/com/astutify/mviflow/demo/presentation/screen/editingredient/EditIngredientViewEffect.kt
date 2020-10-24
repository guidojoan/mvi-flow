package com.astutify.mviflow.demo.presentation.screen.editingredient

sealed class EditIngredientViewEffect {
    object Save : EditIngredientViewEffect()
    object GoBack : EditIngredientViewEffect()
}