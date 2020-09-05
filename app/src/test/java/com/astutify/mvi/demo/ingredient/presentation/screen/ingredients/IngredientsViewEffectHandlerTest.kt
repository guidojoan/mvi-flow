package com.astutify.mvi.demo.ingredient.presentation.screen.ingredients

import com.astutify.mvi.demo.TestHelper
import com.astutify.mvi.demo.domain.interactor.GetIngredientsUseCase
import com.astutify.mvi.demo.domain.interactor.SearchIngredientsUseCase
import com.astutify.mvi.demo.domain.model.Ingredient
import com.astutify.mvi.demo.domain.model.PepperException
import com.astutify.mvi.demo.domain.model.ResponseError
import com.astutify.mvi.demo.domain.model.ResponseSuccessful
import com.astutify.mvi.demo.presentation.Navigator
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEffect
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEffectHandler
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEvent
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class IngredientsViewEffectHandlerTest {

    private val navigator: Navigator = mock()
    private val getIngredientsUseCase: GetIngredientsUseCase = mock()
    private val searchIngredientsUseCase: SearchIngredientsUseCase = mock()
    private val effectHandler =
        IngredientsViewEffectHandler(
            Schedulers.trampoline(),
            navigator,
            getIngredientsUseCase,
            searchIngredientsUseCase
        )
    private val initialState =
        IngredientsViewState()


    @Test
    fun `should return Event with Ingredients when invoked with LoadData Effect`() {
        whenever(getIngredientsUseCase()).thenReturn(Single.just(successfulResponse))

        val result = effectHandler.invoke(initialState,
            IngredientsViewEffect.LoadData
        )
            .test()
            .values()

        assert(result[0] == IngredientsViewEvent.Loading)
        assert((result[1] as IngredientsViewEvent.DataLoaded).ingredients == listOfIngredientsVM)
        verify(getIngredientsUseCase).invoke()
    }

    @Test
    fun `should return LoadingError Event when invoked with LoadData Effect and get error`() {
        whenever(getIngredientsUseCase()).thenReturn(Single.just(errorResponse))

        effectHandler.invoke(initialState,
            IngredientsViewEffect.LoadData
        )
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValues(
                IngredientsViewEvent.Loading,
                IngredientsViewEvent.LoadingError
            )

        verify(getIngredientsUseCase).invoke()
    }

    @Test
    fun `should return Event with ingredients when invoked with Search Effect`() {
        whenever(searchIngredientsUseCase(keyWords)).thenReturn(Single.just(
            successfulResponse
        ))

        effectHandler.invoke(initialState,
            IngredientsViewEffect.Search(
                keyWords
            )
        )
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValue { result ->
                (result as IngredientsViewEvent.DataLoaded).ingredients == listOfIngredientsVM
            }

        verify(searchIngredientsUseCase).invoke(keyWords)
    }

    @Test
    fun `should not return anything when invoked with Search Event and keywords length is not valid`() {
        effectHandler.invoke(initialState,
            IngredientsViewEffect.Search(
                keyWordsNoValidLength
            )
        )
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertNoValues()
    }

    @Test
    fun `should return SearchError Event when invoked with Search Event and get error from Backend`() {
        whenever(searchIngredientsUseCase(keyWords)).thenReturn(Single.just(
            errorResponse
        ))

        effectHandler.invoke(initialState,
            IngredientsViewEffect.Search(
                keyWords
            )
        )
            .cast(IngredientsViewEvent::class.java)
            .test()
            .assertValue(IngredientsViewEvent.SearchError)

        verify(searchIngredientsUseCase).invoke(keyWords)
    }

    @Test
    fun `should navigate to add ingredient without params when invoked with GoToAddIngredient Effect`(){
        effectHandler.invoke(initialState,
            IngredientsViewEffect.GoToAddIngredient
        )
            .test()
            .assertNoErrors()
            .assertNoValues()

        verify(navigator).goToAddIngredient()
    }

    @Test
    fun `should navigate to add ingredient with ingredient as param when invoked with GoToEditIngredient Effect`(){
        effectHandler.invoke(initialState,
            IngredientsViewEffect.GoToEditIngredient(
                ingredientVM
            )
        )
            .test()
            .assertNoErrors()
            .assertNoValues()

        verify(navigator).goToAddIngredient(ingredientVM)
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
