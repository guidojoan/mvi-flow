package com.astutify.mvi.demo.data

import com.astutify.mvi.demo.domain.IngredientRepository

class IngredientDataRepository (
    private val ingredientsApiRepository: IngredientsApiRepository
) : IngredientRepository {

    override fun getIngredients() =
        ingredientsApiRepository.getIngredients()
}
