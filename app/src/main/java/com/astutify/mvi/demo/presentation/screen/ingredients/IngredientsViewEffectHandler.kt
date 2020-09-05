package com.astutify.mvi.demo.presentation.screen.ingredients

import com.astutify.mvi.EffectHandler
import com.astutify.mvi.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mvi.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.model.toPresentation
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler

class IngredientsViewEffectHandler constructor(
    private val main: Scheduler,
    private val navigator: Navigator,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val searchIngredientsUseCase: SearchIngredientsUseCase
) : EffectHandler<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect> {

    override fun invoke(
        state: IngredientsViewState,
        effect: IngredientsViewEffect
    ): Flowable<out IngredientsViewEvent> {
        return when (effect) {
            is IngredientsViewEffect.LoadData -> onLoadData()
            is IngredientsViewEffect.Search -> onSearch(effect)
            is IngredientsViewEffect.GoToAddIngredient -> onGoToAddIngredient()
            is IngredientsViewEffect.GoToEditIngredient -> onGoToEditIngredient(effect)
        }
    }

    private fun onLoadData(): Flowable<IngredientsViewEvent> {
        return getIngredientsUseCase()
            .observeOn(main)
            .toFlowable()
            .map<IngredientsViewEvent> { response ->
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

            }
            .startWith(IngredientsViewEvent.Loading)
    }

    private fun onSearch(effect: IngredientsViewEffect.Search): Flowable<IngredientsViewEvent> {
        return if (effect.name.length > 2) {
            searchIngredientsUseCase(effect.name)
                .observeOn(main)
                .toFlowable()
                .map<IngredientsViewEvent> { response ->
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
            never()
        }
    }

    private fun onGoToEditIngredient(effect: IngredientsViewEffect.GoToEditIngredient): Flowable<IngredientsViewEvent> {
        navigator.goToAddIngredient(effect.ingredient)
        return never()
    }

    private fun onGoToAddIngredient(): Flowable<IngredientsViewEvent> {
        navigator.goToAddIngredient()
        return never()
    }
}
