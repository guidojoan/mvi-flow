package com.astutify.mvi.demo.data

import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.domain.model.Response
import io.reactivex.Single

class IngredientsApiRepository (
    private val api: DevicesApi
) {

    fun getIngredients(): Single<Response<List<Ingredient>>> {
        return api.getIngredients()
            .compose(mapListResponse())
    }
}
