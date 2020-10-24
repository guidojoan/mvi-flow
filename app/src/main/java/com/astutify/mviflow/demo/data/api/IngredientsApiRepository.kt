package com.astutify.mviflow.demo.data.api

import com.astutify.mviflow.demo.data.core.mapListResponse
import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.domain.model.Response
import kotlinx.coroutines.flow.Flow

class IngredientsApiRepository(
    private val api: DevicesApi
) {
    fun getIngredients(): Flow<Response<List<Ingredient>>> =
        mapListResponse(api.getIngredients())
}
