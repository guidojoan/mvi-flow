package com.astutify.mvi.demo.presentation.screen.ingredients

import com.astutify.mvi.Effect
import com.astutify.mvi.Reducer
import com.astutify.mvi.ReducerResult
import com.astutify.mvi.State

class IngredientsViewReducer: Reducer<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>() {

    override fun invoke(
        state: IngredientsViewState,
        event: IngredientsViewEvent
    ): ReducerResult<IngredientsViewState, IngredientsViewEffect> {
        return when (event) {
            is IngredientsViewEvent.LoadData -> onLoadData()
            is IngredientsViewEvent.Search -> onSearch(event)
            is IngredientsViewEvent.Loading -> onLoading(state)
            is IngredientsViewEvent.DataLoaded -> onDataLoaded(state, event)
            is IngredientsViewEvent.LoadingError -> onLoadingError(state)
            is IngredientsViewEvent.ClickAddIngredient -> onClickAddIngredient()
            is IngredientsViewEvent.IngredientClicked -> onIngredientClicked(event)
            is IngredientsViewEvent.IngredientAdded -> onIngredientAdded(event, state)
            IngredientsViewEvent.ClickRefresh -> onLoadData()
            IngredientsViewEvent.ClickCloseSearch -> onLoadData()
            IngredientsViewEvent.SearchError -> onSearchError(state)
        }
    }

    private fun onLoadData(): Effect<IngredientsViewState, IngredientsViewEffect> =
        Effect(IngredientsViewEffect.LoadData)

    private fun onSearch(event: IngredientsViewEvent.Search): Effect<IngredientsViewState, IngredientsViewEffect> =
        Effect(IngredientsViewEffect.Search(event.name))

    private fun onLoading(state: IngredientsViewState): State<IngredientsViewState, IngredientsViewEffect> =
        State(state.copyState(status = IngredientsViewState.Status.LOADING))

    private fun onDataLoaded(
        state: IngredientsViewState,
        event: IngredientsViewEvent.DataLoaded
    ): State<IngredientsViewState, IngredientsViewEffect> =
        State(
            state.copyState(
                status = if (event.ingredients.isEmpty()) IngredientsViewState.Status.NO_RESULTS else null,
                ingredients = event.ingredients
            )
        )

    private fun onLoadingError(state: IngredientsViewState): State<IngredientsViewState, IngredientsViewEffect> =
        State(state.copyState(status = IngredientsViewState.Status.LOADING_ERROR))

    private fun onClickAddIngredient(): Effect<IngredientsViewState, IngredientsViewEffect> =
        Effect(IngredientsViewEffect.GoToAddIngredient)

    private fun onIngredientClicked(event: IngredientsViewEvent.IngredientClicked): Effect<IngredientsViewState, IngredientsViewEffect> =
        Effect(IngredientsViewEffect.GoToEditIngredient(event.ingredient))

    private fun onIngredientAdded(
        event: IngredientsViewEvent.IngredientAdded,
        state: IngredientsViewState
    ): State<IngredientsViewState, IngredientsViewEffect> =
            State(state.copyWithIngredient(event.ingredient))

    private fun onSearchError(state: IngredientsViewState): State<IngredientsViewState, IngredientsViewEffect> =
        State(state.copyState(status = IngredientsViewState.Status.SEARCH_ERROR))
}
