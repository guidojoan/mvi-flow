package com.astutify.mvi.demo.presentation.screen.editingredient

sealed class EditIngredientViewEvent {
    object ClickBack : EditIngredientViewEvent()
    object Save : EditIngredientViewEvent()
    object LoadingSave : EditIngredientViewEvent()
    object ErrorSave : EditIngredientViewEvent()
    data class NameChange(val name: String) : EditIngredientViewEvent()
}