package com.astutify.mvi.demo.presentation.screen.editingredient

import com.astutify.mvi.EffectHandler
import com.astutify.mvi.demo.Mockable
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import io.reactivex.Flowable
import io.reactivex.Flowable.never

@Mockable
class EditIngredientViewEffectHandler (
    private val navigator: Navigator
) : EffectHandler<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect> {

    override fun invoke(
        state: EditIngredientViewState,
        effect: EditIngredientViewEffect
    ): Flowable<out EditIngredientViewEvent> {
        return when (effect) {
            is EditIngredientViewEffect.Save -> saveIngredient(state.ingredient)
            EditIngredientViewEffect.GoBack -> onGoBack()
        }
    }

    private fun onGoBack(): Flowable<EditIngredientViewEvent> {
        navigator.goBack()
        return never()
    }

    private fun saveIngredient(ingredient: IngredientViewModel): Flowable<EditIngredientViewEvent> {
        navigator.finishAddIngredient(ingredient)
        return never()
    }
}