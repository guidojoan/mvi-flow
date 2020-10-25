package com.astutify.mviflow.demo.presentation.screen.editingredient

import com.astutify.mviflow.EffectHandler
import com.astutify.mviflow.demo.presentation.Navigator
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel
import com.astutify.mviflow.demo.utils.test.Mockable
import com.astutify.mviflow.utils.terminalEvent
import kotlinx.coroutines.flow.Flow

@Mockable
class EditIngredientViewEffectHandler (
    private val navigator: Navigator
) : EffectHandler<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect> {

    override fun invoke(
        state: EditIngredientViewState,
        effect: EditIngredientViewEffect
    ): Flow<EditIngredientViewEvent> {
        return when (effect) {
            is EditIngredientViewEffect.Save -> saveIngredient(state.ingredient)
            EditIngredientViewEffect.GoBack -> onGoBack()
        }
    }

    private fun onGoBack(): Flow<EditIngredientViewEvent> {
        navigator.goBack()
        return terminalEvent()
    }

    private fun saveIngredient(ingredient: IngredientViewModel): Flow<EditIngredientViewEvent> {
        navigator.finishAddIngredient(ingredient)
        return terminalEvent()
    }
}