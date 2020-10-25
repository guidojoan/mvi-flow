package com.astutify.mviflow.demo.presentation.screen.ingredients

import com.astutify.mviflow.EffectHandler
import com.astutify.mviflow.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mviflow.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mviflow.demo.presentation.Navigator
import com.astutify.mviflow.demo.presentation.model.toPresentation
import com.astutify.mviflow.utils.startWithEvent
import com.astutify.mviflow.utils.terminalEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class IngredientsViewEffectHandler constructor(
    private val navigator: Navigator,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val searchIngredientsUseCase: SearchIngredientsUseCase
) : EffectHandler<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect> {

    override fun invoke(
        state: IngredientsViewState,
        effect: IngredientsViewEffect
    ): Flow<IngredientsViewEvent> {
        return when (effect) {
            is IngredientsViewEffect.LoadData -> onLoadData()
            is IngredientsViewEffect.Search -> onSearch(effect)
            is IngredientsViewEffect.GoToAddIngredient -> onGoToAddIngredient()
            is IngredientsViewEffect.GoToEditIngredient -> onGoToEditIngredient(effect)
        }
    }

    private fun onLoadData(): Flow<IngredientsViewEvent> {
        return getIngredientsUseCase()
            .flowOn(Dispatchers.IO)
            .map { response ->
                response.fold(
                    {
                        IngredientsViewEvent.DataLoaded(
                            it.map {
                                toPresentation(
                                    it
                                )
                            })
                    },
                    {
                        IngredientsViewEvent.LoadingError
                    }
                )
            }.startWithEvent(IngredientsViewEvent.Loading)
    }

    private fun onSearch(effect: IngredientsViewEffect.Search): Flow<IngredientsViewEvent> {
        return if (effect.name.length > 2) {
            searchIngredientsUseCase(effect.name)
                .flowOn(Dispatchers.IO)
                .map { response ->
                    response.fold({ ingredients ->
                        IngredientsViewEvent.DataLoaded(
                            ingredients.map {
                                toPresentation(
                                    it
                                )
                            }
                        )
                    }, {
                        IngredientsViewEvent.SearchError
                    })
                }
        } else {
            terminalEvent()
        }
    }

    private fun onGoToEditIngredient(effect: IngredientsViewEffect.GoToEditIngredient): Flow<IngredientsViewEvent> {
        navigator.goToAddIngredient(effect.ingredient)
        return terminalEvent()
    }

    private fun onGoToAddIngredient(): Flow<IngredientsViewEvent> {
        navigator.goToAddIngredient()
        return terminalEvent()
    }
}
