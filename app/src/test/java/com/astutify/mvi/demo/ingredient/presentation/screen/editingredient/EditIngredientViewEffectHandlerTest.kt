package com.astutify.mvi.demo.ingredient.presentation.screen.editingredient

import com.astutify.mvi.demo.TestHelper
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEffect
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEffectHandler
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class EditIngredientViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val effectHandler = EditIngredientViewEffectHandler(navigator)
    private val initialState = EditIngredientViewState(ingredient)

    @Test
    fun `should navigate back when invoked with GoBack Effect`() {
        effectHandler.invoke(initialState, EditIngredientViewEffect.GoBack)
            .test()
            .assertNoErrors()
            .assertNoValues()

        verify(navigator).goBack()
    }

    @Test
    fun `should finish with an ingredient as result when invoked with Save Effect`() {
        effectHandler.invoke(initialState, EditIngredientViewEffect.Save)
            .test()
            .assertNoErrors()
            .assertNoValues()

        verify(navigator).finishAddIngredient(ingredient)
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(navigator)
    }

    companion object {
        private val ingredient = TestHelper.getIngredientVM()
    }
}
