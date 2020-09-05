package com.astutify.mvi.demo.ingredient.presentation.screen.ingredients

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.TestHelper
import com.astutify.mvi.demo.ingredient.MockApp
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsView
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewEvent
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewFeature
import com.astutify.mvi.demo.presentation.screen.ingredients.IngredientsViewState
import com.astutify.mvi.demo.presentation.screen.ingredients.di.IngredientsViewComponent
import com.astutify.mvi.demo.presentation.screen.ingredients.list.IngredientItemViewHolder
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.stubbing.Answer

class IngredientsViewTest {

    private val stateSubject = BehaviorSubject.createDefault(IngredientsViewState())
    private val viewController: IngredientsViewFeature = mock {
        on { subscribe(any()) } doAnswer (
                Answer {
                    val observer = it.arguments[0] as Observer<IngredientsViewState>
                    stateSubject.subscribe(observer)
                }
                )
    }

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<IngredientsView> =
        object : ActivityTestRule<IngredientsView>(IngredientsView::class.java) {

            override fun beforeActivityLaunched() {
                ApplicationProvider.getApplicationContext<MockApp>().appComponent = mock {
                    on { ingredientsViewBuilder() } doReturn IngredientsViewComponentBuilder(
                        viewController
                    )
                }
            }
        }



    private fun runOnUiThread(runnable: () -> Unit) {
        activityRule.runOnUiThread(runnable)
    }

    @Test
    fun showLoading() {
        runOnUiThread {
            stateSubject.onNext(
                IngredientsViewState(
                    status = IngredientsViewState.Status.LOADING
                )
            )
        }

        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun showEmptyState() {
        runOnUiThread {
            stateSubject.onNext(
                IngredientsViewState(
                    status = IngredientsViewState.Status.NO_RESULTS
                )
            )
        }

        onView(withText(R.string.empty_state_ingredients)).check(matches(isDisplayed()))
    }

    @Test
    fun showNetworkError() {
        runOnUiThread {
            stateSubject.onNext(
                IngredientsViewState(
                    status = IngredientsViewState.Status.LOADING_ERROR
                )
            )
        }

        onView(withText(R.string.generic_network_error)).check(matches(isDisplayed()))
    }

    @Test
    fun swipeToRefresh() {
        runOnUiThread {
            stateSubject.onNext(
                IngredientsViewState(
                    listOfIngredients
                )
            )
        }
        onView(withId(R.id.swipeToRefresh)).perform(swipeDown())

        verify(viewController).accept(IngredientsViewEvent.ClickRefresh)
    }

    @Test
    fun clickAddIngredient(){
        runOnUiThread {
            stateSubject.onNext(IngredientsViewState())
        }
        onView(withId(R.id.buttonAddIngredient)).perform(click())

        verify(viewController).accept(IngredientsViewEvent.ClickAddIngredient)
    }

    @Test
    fun clickOnIngredient() {
        val argumentCaptor = argumentCaptor<IngredientsViewEvent>()

        runOnUiThread {
            stateSubject.onNext(
                IngredientsViewState(
                    listOfIngredients
                )
            )
        }
        onView(withId(R.id.ingredients))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<IngredientItemViewHolder>(
                    0,
                    click()
                )
            )

        verify(viewController).accept(argumentCaptor.capture())
        assertEquals(listOfIngredients.first(), (argumentCaptor.firstValue as IngredientsViewEvent.IngredientClicked).ingredient)
    }

    @Test
    fun search(){
        val argumentCaptor = argumentCaptor<IngredientsViewEvent>()

        runOnUiThread {
            stateSubject.onNext(IngredientsViewState())
        }
        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.search_src_text)).perform(ViewActions.typeText(searchCharacter))

        verify(viewController).accept(argumentCaptor.capture())
        assertEquals(searchCharacter, (argumentCaptor.firstValue as IngredientsViewEvent.Search).name)
    }

    @Test
    fun clickCloseSearch(){
        runOnUiThread {
            stateSubject.onNext(IngredientsViewState())
        }

        onView(withId(R.id.searchBar)).perform(click())
        onView(withId(R.id.search_close_btn)).perform(click())

        verify(viewController).accept(IngredientsViewEvent.ClickCloseSearch)
    }

    companion object {
        private val listOfIngredients = listOf(TestHelper.getIngredientVM())
        private const val searchCharacter = "c"
    }
}

private class IngredientsViewComponentBuilder(
    private val controller: IngredientsViewFeature
) : IngredientsViewComponent.Builder {

    override fun withActivity(view: AppCompatActivity): IngredientsViewComponent.Builder =
        this

    override fun build(): IngredientsViewComponent =
        object : IngredientsViewComponent {

            override fun inject(view: IngredientsView) {
                view.controller = controller
            }
        }
}
