package com.astutify.mvi.demo.presentation.screen.ingredients.di

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mvi.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEffectHandler
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewFeature
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewReducer
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class IngredientsViewModule {

    @Provides
    fun providesIngredientsViewReducer() =
        IngredientsViewReducer()

    @Provides
    fun providesIngredientsViewEffectHandler(
        @Named("ui_thread")  main: Scheduler,
        navigator: Navigator,
        getIngredientsUseCase: GetIngredientsUseCase,
        searchIngredientsUseCase: SearchIngredientsUseCase
    ) =
        IngredientsViewEffectHandler(
            main,
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
