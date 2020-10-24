package com.astutify.mviflow.demo.data.api

import com.astutify.mviflow.demo.data.model.IngredientEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface DevicesApi {

    @GET("demo/ingredients.json")
    fun getIngredients(): Flow<Response<List<IngredientEntity>>>
}
