package com.astutify.mvi.demo

import com.astutify.mvi.demo.domain.IngredientRepository
import com.astutify.mvi.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mvi.demo.domain.interactor.SearchIngredientsUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun providesGetIngredientsUseCase(
        api: IngredientRepository
    ) = GetIngredientsUseCase(api)

    @Provides
    fun providesSearchIngredientsUseCase(
        api: IngredientRepository
    ) = SearchIngredientsUseCase(api)
}
