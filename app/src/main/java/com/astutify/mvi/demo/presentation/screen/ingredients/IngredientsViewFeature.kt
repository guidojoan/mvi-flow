package com.astutify.mvi.demo.presentation.screen.ingredients

import com.astutify.mvi.Feature
import com.astutify.mvi.demo.Mockable

@Mockable
class IngredientsViewFeature constructor(
    reducer: IngredientsViewReducer,
    effectHandler: IngredientsViewEffectHandler
) : Feature<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>(
    initialState = IngredientsViewState(),
    reducer = reducer,
    effectHandler = effectHandler
) {
    override fun initialEvent() = IngredientsViewEvent.LoadData
}
