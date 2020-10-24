package com.astutify.mviflow.demo.domain.interactor

import com.astutify.mviflow.demo.domain.IngredientRepository
import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.domain.model.Response
import com.astutify.mviflow.demo.utils.test.Mockable
import kotlinx.coroutines.flow.Flow

@Mockable
class GetIngredientsUseCase (
    private val api: IngredientRepository
) {
    operator fun invoke(): Flow<Response<List<Ingredient>>> {
        return api.getIngredients()
    }
}
