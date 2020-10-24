package com.astutify.mviflow.demo.ingredient.presentation.screen.ingredients

import com.astutify.mviflow.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mviflow.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mviflow.demo.domain.model.Ingredient
import com.astutify.mviflow.demo.domain.model.PepperException
import com.astutify.mviflow.demo.domain.model.ResponseError
import com.astutify.mviflow.demo.domain.model.ResponseSuccessful
import com.astutify.mviflow.demo.presentation.Navigator
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewEffect
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewEffectHandler
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewEvent
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewState
import com.astutify.mviflow.demo.utils.test.TestHelper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class IngredientsViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val getIngredientsUseCase: GetIngredientsUseCase = mock()
    private val searchIngredientsUseCase: SearchIngredientsUseCase = mock()
    private val effectHandler =
        IngredientsViewEffectHandler(
            navigator,
            getIngredientsUseCase,
            searchIngredientsUseCase
        )
    private val initialState =
        IngredientsViewState()


    @Test
    fun `should return Event with Ingredients when invoked with LoadData Effect`() {
        runBlocking {
            whenever(getIngredientsUseCase()).thenReturn(flowOf(successfulResponse))

            val result = effectHandler.invoke(initialState, IngredientsViewEffect.LoadData).toList()

            assert(result[0] == IngredientsViewEvent.Loading)
            assert((result[1] as IngredientsViewEvent.DataLoaded).ingredients == listOfIngredientsVM)
            verify(getIngredientsUseCase).invoke()
        }
    }

    @Test
    fun `should return LoadingError Event when invoked with LoadData Effect and get error`() {
        runBlocking {
            whenever(getIngredientsUseCase()).thenReturn(flowOf(errorResponse))

            val result = effectHandler.invoke(initialState, IngredientsViewEffect.LoadData).toList()

            assert(result[0] == IngredientsViewEvent.Loading)
            assert(result[1] == IngredientsViewEvent.LoadingError)
            verify(getIngredientsUseCase).invoke()
        }
    }

    @Test
    fun `should return Event with ingredients when invoked with Search Effect`() {
        runBlocking {
            whenever(searchIngredientsUseCase(keyWords)).thenReturn(flowOf(successfulResponse))

            val result = effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyWords)).toList()

            assert((result[0] as IngredientsViewEvent.DataLoaded).ingredients == listOfIngredientsVM)
            verify(searchIngredientsUseCase).invoke(keyWords)
        }
    }

    @Test
    fun `should not return anything when invoked with Search Event and keywords length is not valid`() {
        runBlocking {
            val result = effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyWordsNoValidLength)).toList()

            assert(result.isEmpty())
        }
    }

    @Test
    fun `should return SearchError Event when invoked with Search Event and get error from Backend`() {
        runBlocking {
            whenever(searchIngredientsUseCase(keyWords)).thenReturn(flowOf(errorResponse))

            val result = effectHandler.invoke(initialState, IngredientsViewEffect.Search(keyWords)).toList()

            assert(result[0] == IngredientsViewEvent.SearchError)
            verify(searchIngredientsUseCase).invoke(keyWords)
        }
    }

    @Test
    fun `should navigate to add ingredient without params when invoked with GoToAddIngredient Effect`(){
        runBlocking {
            val result = effectHandler.invoke(initialState, IngredientsViewEffect.GoToAddIngredient).toList()

            assert(result.isEmpty())
            verify(navigator).goToAddIngredient()
        }
    }

    @Test
    fun `should navigate to add ingredient with ingredient as param when invoked with GoToEditIngredient Effect`(){
        runBlocking {
            val result = effectHandler.invoke(initialState, IngredientsViewEffect.GoToEditIngredient(ingredientVM)).toList()

            assert(result.isEmpty())
            verify(navigator).goToAddIngredient(ingredientVM)
        }
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(navigator, getIngredientsUseCase, searchIngredientsUseCase)
    }

    companion object {
        private val ingredientVM = TestHelper.getIngredientVM()
        private val listOfIngredients = listOf(TestHelper.getIngredient())
        private val listOfIngredientsVM = listOf(TestHelper.getIngredientVM())
        private val successfulResponse =
            ResponseSuccessful(
                listOfIngredients
            )
        private val errorResponse =
            ResponseError<List<Ingredient>>(
                error = PepperException.NetworkError
            )
        private const val keyWordsNoValidLength = "Pl"
        private const val keyWords = "Queso"
    }
}
