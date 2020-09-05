package com.astutify.mvi.demo.presentation.screen.editingredient.di

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientView
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewState
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent( modules = [EditIngredientViewModule::class])
interface EditIngredientViewComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun withActivity(view: AppCompatActivity): Builder

        @BindsInstance
        fun withIngredient(view: IngredientViewModel?): Builder

        @BindsInstance
        fun withInitialState(state: EditIngredientViewState?): Builder

        fun build(): EditIngredientViewComponent
    }

    fun inject(view: EditIngredientView)
}