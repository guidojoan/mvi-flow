package com.astutify.mviflow.demo.presentation.model

import com.astutify.mviflow.demo.domain.model.Ingredient

fun toPresentation(ingredient: Ingredient) = IngredientViewModel(id = ingredient.id, name = ingredient.name)
