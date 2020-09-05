package com.astutify.mvi.demo.presentation.screen.editingredient.di

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEffectHandler
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewFeature
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewReducer
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewState
import dagger.Module
import dagger.Provides

@Module
class EditIngredientViewModule {

    @Provides
    fun providesEditIngredientViewReducer() = EditIngredientViewReducer()

    @Provides
    fun providesEditIngredientViewEffectHandler(
        navigator: Navigator
    ) = EditIngredientViewEffectHandler(navigator)

    @Provides
    fun providesEditIngredientViewFeature(
        ingredient: IngredientViewModel?,
        initialState: EditIngredientViewState?,
        reducer: EditIngredientViewReducer,
        effectHandler: EditIngredientViewEffectHandler
    ) = EditIngredientViewFeature(ingredient, initialState, reducer, effectHandler)

    @Provides
    fun providesNavigator(activity: AppCompatActivity) = Navigator(activity)
}
