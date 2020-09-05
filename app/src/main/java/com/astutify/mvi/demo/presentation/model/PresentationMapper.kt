package com.astutify.mvi.demo.presentation.model

import com.astutify.mvi.demo.domain.model.Ingredient

fun toPresentation(ingredient: Ingredient) = IngredientViewModel(id = ingredient.id, name = ingredient.name)
