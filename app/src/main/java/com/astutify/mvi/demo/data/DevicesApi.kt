package com.astutify.mvi.demo.data

import com.astutify.mvi.demo.data.model.IngredientEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface DevicesApi {

    @GET("demo/ingredients.json")
    fun getIngredients(): Single<Response<List<IngredientEntity>>>
}
