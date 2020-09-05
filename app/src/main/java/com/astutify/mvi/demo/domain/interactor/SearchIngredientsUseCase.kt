package com.astutify.mvi.demo.domain.interactor

import com.astutify.mvi.demo.Mockable
import com.astutify.mvi.demo.domain.IngredientRepository
import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.domain.model.Response
import io.reactivex.Single

@Mockable
class SearchIngredientsUseCase (
    private val api: IngredientRepository
) {
    operator fun invoke(keyWords: String? = null): Single<Response<List<Ingredient>>> {
        return api.getIngredients().map {
            it.mapSuccessFull { ingredients ->
                ingredients.filter { it.name.contains(keyWords!!, true) }
            }
        }
    }
}
