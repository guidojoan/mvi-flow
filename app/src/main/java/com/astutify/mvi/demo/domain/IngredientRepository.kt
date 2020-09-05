package com.astutify.mvi.demo.domain

import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.domain.model.Response
import io.reactivex.Single

interface IngredientRepository {

    fun getIngredients(): Single<Response<List<Ingredient>>>
}
