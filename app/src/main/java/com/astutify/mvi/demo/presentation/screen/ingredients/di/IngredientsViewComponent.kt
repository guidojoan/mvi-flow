package com.astutify.mvi.demo.presentation.screen.ingredients.di

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsView
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [IngredientsViewModule::class])
interface IngredientsViewComponent {
    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun withActivity(view: AppCompatActivity): Builder

        fun build(): IngredientsViewComponent
    }

    fun inject(view: IngredientsView)
}