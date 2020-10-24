package com.astutify.mviflow.demo.presentation.screen.ingredients

import com.astutify.mviflow.Feature
import com.astutify.mviflow.demo.utils.test.Mockable

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
