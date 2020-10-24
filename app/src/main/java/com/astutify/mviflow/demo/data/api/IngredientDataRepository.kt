package com.astutify.mviflow.demo.data.api

import com.astutify.mviflow.demo.domain.IngredientRepository

class IngredientDataRepository (
    private val ingredientsApiRepository: IngredientsApiRepository
) : IngredientRepository {

    override fun getIngredients() =
        ingredientsApiRepository.getIngredients()
}
