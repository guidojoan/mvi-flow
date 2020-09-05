package com.astutify.mvi.demo.ingredient.presentation.screen.ingredients

import com.astutify.mvi.demo.TestHelper
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEffect
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEvent
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewReducer
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewState
import org.junit.jupiter.api.Test

class IngredientsViewReducerTest {

    private val reducer =
        IngredientsViewReducer()
    private val initialState =
        IngredientsViewState()

    @Test
    fun `should return LoadData Effect when is invoked with LoadData Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.LoadData
        )

        assert(result.effect is IngredientsViewEffect.LoadData)
    }

    @Test
    fun `should return Search Effect when is invoked with Search Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.Search(
                keyWords
            )
        )

        assert((result.effect as IngredientsViewEffect.Search).name == keyWords)
    }

    @Test
    fun `should return Loading State when is invoked with Loading Event`() {
        val expectedState = initialState.copyState(status = IngredientsViewState.Status.LOADING)
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.Loading
        )

        assert(result.state == expectedState)
    }

    @Test
    fun `should return State with ingredients when is invoked with DataLoaded Event`() {
        val expectedState = initialState.copyState(ingredients = listOfIngredients)
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.DataLoaded(
                listOfIngredients
            )
        )

        assert(result.state == expectedState)
    }

    @Test
    fun `should return LoadingError State when is invoked with LoadingError Event`() {
        val expectedState = initialState.copyState(status = IngredientsViewState.Status.LOADING_ERROR)
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.LoadingError
        )

        assert(result.state == expectedState)
    }

    @Test
    fun `should return GoToAddIngredient Effect when is invoked with ClickAddIngredient Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.ClickAddIngredient
        )

        assert(result.effect is IngredientsViewEffect.GoToAddIngredient)
    }

    @Test
    fun `should return GoToEditIngredient Effect when is invoked with IngredientClicked Event`() {
        val result =
            reducer.invoke(initialState,
                IngredientsViewEvent.IngredientClicked(
                    ingredient
                )
            )

        assert((result.effect as IngredientsViewEffect.GoToEditIngredient).ingredient == ingredient)
    }

    @Test
    fun `should return State with Ingredient when is invoked with IngredientAdded Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.IngredientAdded(
                ingredient
            )
        )

        assert(result.state!!.ingredients.first() == ingredient)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickRefresh Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.ClickRefresh
        )

        assert(result.effect is IngredientsViewEffect.LoadData)
    }

    @Test
    fun `should return LoadData Effect when is invoked with ClickCloseSearch Event`() {
        val result = reducer.invoke(initialState,
            IngredientsViewEvent.ClickCloseSearch
        )

        assert(result.effect is IngredientsViewEffect.LoadData)
    }

    @Test
    fun `should return No Results State when is invoked with DataLoaded Event and ingredients is empty`() {
        val event =
            IngredientsViewEvent.DataLoaded(
                listOf()
            )
        val expectedState = initialState.copyState(
            status = IngredientsViewState.Status.NO_RESULTS,
            ingredients = event.ingredients
        )
        val result = reducer.invoke(initialState, event)

        assert(result.state == expectedState)
    }

    companion object {
        private const val keyWords = "Tomato"
        private val ingredient = TestHelper.getIngredientVM()
        private val listOfIngredients = listOf(TestHelper.getIngredientVM())
    }
}
