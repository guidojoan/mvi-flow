package com.astutify.mviflow.demo.di

import com.astutify.mviflow.demo.domain.IngredientRepository
import com.astutify.mviflow.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mviflow.demo.domain.interactor.SearchIngredientsUseCase
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
