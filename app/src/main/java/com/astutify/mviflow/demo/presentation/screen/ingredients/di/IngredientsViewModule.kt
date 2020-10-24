package com.astutify.mviflow.demo.presentation.screen.ingredients.di

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mviflow.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mviflow.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mviflow.demo.presentation.Navigator
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewEffectHandler
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewFeature
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewReducer
import dagger.Module
import dagger.Provides

@Module
class IngredientsViewModule {

    @Provides
    fun providesIngredientsViewReducer() =
        IngredientsViewReducer()

    @Provides
    fun providesIngredientsViewEffectHandler(
        navigator: Navigator,
        getIngredientsUseCase: GetIngredientsUseCase,
        searchIngredientsUseCase: SearchIngredientsUseCase
    ) =
        IngredientsViewEffectHandler(
            navigator,
            getIngredientsUseCase,
            searchIngredientsUseCase
        )

    @Provides
    fun providesIngredientsViewFeature(
        reducer: IngredientsViewReducer,
        effectHandler: IngredientsViewEffectHandler
    ) =
        IngredientsViewFeature(
            reducer,
            effectHandler
        )

    @Provides
    fun providesNavigator(activity: AppCompatActivity) = Navigator(activity)
}
