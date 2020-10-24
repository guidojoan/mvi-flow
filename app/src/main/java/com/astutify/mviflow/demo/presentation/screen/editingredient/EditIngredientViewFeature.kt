package com.astutify.mviflow.demo.presentation.screen.editingredient

import com.astutify.mviflow.Feature
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel
import com.astutify.mviflow.demo.utils.test.Mockable

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