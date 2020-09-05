package com.astutify.mvi.demo.presentation.screen.editingredient

import com.astutify.mvi.Feature
import com.astutify.mvi.demo.Mockable
import com.astutify.mvi.demo.presentation.model.IngredientViewModel

@Mockable
class EditIngredientViewFeature (
    ingredient: IngredientViewModel?,
    initialState: EditIngredientViewState?,
    reducer: EditIngredientViewReducer,
    effectHandler: EditIngredientViewEffectHandler
) : Feature<EditIngredientViewState, EditIngredientViewEvent, EditIngredientViewEffect>(
    initialState = when {
        initialState != null -> initialState
        ingredient != null -> EditIngredientViewState(
            ingredient = ingredient,
            mode = EditIngredientViewState.Mode.EDIT
        )
        else -> EditIngredientViewState()
    },
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() : EditIngredientViewEvent? = null
}