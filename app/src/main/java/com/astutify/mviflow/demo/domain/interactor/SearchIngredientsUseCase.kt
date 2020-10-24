package com.astutify.mviflow.demo.domain.interactor

import com.astutify.mviflow.demo.domain.IngredientRepository
import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.domain.model.Response
import com.astutify.mviflow.demo.utils.test.Mockable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Mockable
class SearchIngredientsUseCase(
    private val api: IngredientRepository
) {
    operator fun invoke(keyWords: String? = null): Flow<Response<List<Ingredient>>> {
        return api.getIngredients().map {
            it.mapSuccessFull { ingredients ->
                ingredients.filter { it.name.contains(keyWords!!, true) }
            }
        }
    }
}
