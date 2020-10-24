package com.astutify.mviflow.demo.domain

import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    fun getIngredients(): Flow<Response<List<Ingredient>>>
}
