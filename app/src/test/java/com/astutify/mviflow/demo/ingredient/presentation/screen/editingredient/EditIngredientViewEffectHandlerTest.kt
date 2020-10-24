package com.astutify.mviflow.demo.ingredient.presentation.screen.editingredient

import com.astutify.mviflow.demo.presentation.Navigator
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewEffect
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewEffectHandler
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewState
import com.astutify.mviflow.demo.utils.test.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class EditIngredientViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val effectHandler = EditIngredientViewEffectHandler(navigator)
    private val initialState = EditIngredientViewState(ingredient)

    @Test
    fun `should navigate back when invoked with GoBack Effect`() {
        runBlocking {
            effectHandler.invoke(initialState, EditIngredientViewEffect.GoBack).collect ()

            verify(navigator).goBack()
        }
    }

    @Test
    fun `should finish with an ingredient as result when invoked with Save Effect`() {
        runBlocking {
            effectHandler.invoke(initialState, EditIngredientViewEffect.Save).collect()

            verify(navigator).finishAddIngredient(ingredient)
        }
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(navigator)
    }

    companion object {
        private val ingredient = TestHelper.getIngredientVM()
    }
}
