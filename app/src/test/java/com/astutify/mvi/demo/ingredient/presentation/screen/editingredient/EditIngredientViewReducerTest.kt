package com.astutify.mvi.demo.ingredient.presentation.screen.editingredient

import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEffect
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEvent
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewReducer
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EditIngredientViewReducerTest {

    private val initialState = EditIngredientViewState()
    private val reducer = EditIngredientViewReducer()

    @Test
    fun `should return Save Effect when invoked with Save Event`() {
        val result = reducer(initialState, EditIngredientViewEvent.Save)

        assert(result.effect is EditIngredientViewEffect.Save)
    }

    @Test
    fun `should return Loading State when invoked with LoadingSave Event`() {
        val expectedState = initialState.copyState(status = EditIngredientViewState.Status.LOADING)

        val result = reducer(initialState, EditIngredientViewEvent.LoadingSave)

        assertEquals(expectedState, result.state)
    }

    @Test
    fun `should return Error Save State when invoked with ErrorSave Event`() {
        val expectedState =
            initialState.copyState(status = EditIngredientViewState.Status.ERROR_SAVE)

        val result = reducer(initialState, EditIngredientViewEvent.ErrorSave)

        assertEquals(expectedState, result.state)
    }

    @Test
    fun `should return State with a name updated and save button enabled when invoked with NameChanged Event`() {
        val expectedState = initialState.copyState(
            ingredient = initialState.ingredient.copy(name = ingredientName),
            saveEnabled = true
        )

        val result = reducer(initialState, EditIngredientViewEvent.NameChange(ingredientName))

        assertEquals(expectedState, result.state)
    }

    @Test
    fun `should return State with a name updated and save button disabled when invoked with NameChanged Event and name length is not valid`() {
        val expectedState = initialState.copyState(
            ingredient = initialState.ingredient.copy(name = ingredientNameInvalidLength),
            saveEnabled = false
        )

        val result =
            reducer(initialState, EditIngredientViewEvent.NameChange(ingredientNameInvalidLength))

        assertEquals(expectedState, result.state)
    }


    @Test
    fun `should return GoBack Effect when invoked with ClickBack Event`() {
        val result = reducer(initialState, EditIngredientViewEvent.ClickBack)

        assert(result.effect is EditIngredientViewEffect.GoBack)
    }

    companion object {
        private const val ingredientName = "Tomate"
        private const val ingredientNameInvalidLength = "To"
    }
}
