package com.astutify.mvi.demo.presentation.screen.editingredient

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State
import com.astutify.mvi.demo.Mockable

@Mockable
class EditIngredientViewReducer: Reducer<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect>() {

    override fun invoke(
        state: EditIngredientViewState,
        event: EditIngredientViewEvent
    ): ReducerResult<EditIngredientViewState, EditIngredientViewEffect> {
        return when (event) {
            is EditIngredientViewEvent.Save -> onSave()
            EditIngredientViewEvent.LoadingSave -> onLoadingSave(state)
            is EditIngredientViewEvent.ErrorSave -> onErrorSave(state)
            is EditIngredientViewEvent.NameChange -> onNameChange(state, event)
            EditIngredientViewEvent.ClickBack -> onClickBack()

        }
    }

    private fun onSave(): Effect<EditIngredientViewState, EditIngredientViewEffect> =
        Effect(EditIngredientViewEffect.Save)

    private fun onLoadingSave(state: EditIngredientViewState): State<EditIngredientViewState, EditIngredientViewEffect> {
        return State(
            state.copyState(
                status = EditIngredientViewState.Status.LOADING
            )
        )
    }

    private fun onErrorSave(state: EditIngredientViewState): State<EditIngredientViewState, EditIngredientViewEffect> {
        return State(
            state.copyState(
                status = EditIngredientViewState.Status.ERROR_SAVE
            )
        )
    }

    private fun onNameChange(
        state: EditIngredientViewState,
        event: EditIngredientViewEvent.NameChange
    ): State<EditIngredientViewState, EditIngredientViewEffect> {
        return State(
            state = state.copyState(
                ingredient = state.ingredient.copy(name = event.name),
                saveEnabled = event.name.length > 5
            )
        )
    }

    private fun onClickBack(): Effect<EditIngredientViewState, EditIngredientViewEffect> =
        Effect(EditIngredientViewEffect.GoBack)
}