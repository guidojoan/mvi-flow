package com.astutify.mviflow.demo.di

import com.astutify.mviflow.demo.presentation.screen.editingredient.di.EditIngredientViewComponent
import com.astutify.mviflow.demo.presentation.screen.ingredients.di.IngredientsViewComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
    }

    fun ingredientsViewBuilder(): IngredientsViewComponent.Builder

    fun editIngredientViewBuilder(): EditIngredientViewComponent.Builder
}
