package com.astutify.mviflow.demo.ingredient.presentation.screen.ingredients

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.astutify.mviflow.demo.R
import com.astutify.mviflow.demo.ingredient.MockApp
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsView
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewEvent
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewFeature
import com.astutify.mviflow.demo.presentation.screen.ingredients.IngredientsViewState
import com.astutify.mviflow.demo.presentation.screen.ingredients.di.IngredientsViewComponent
import com.astutify.mviflow.demo.presentation.screen.ingredients.list.IngredientItemViewHolder
import com.astutify.mviflow.demo.utils.test.TestHelper
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class IngredientsViewTest {

    private val stateSubject = MutableStateFlow(IngredientsViewState())
    private val viewController: IngredientsViewFeature = mock {
        on { connect() } doReturn stateSubject
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

    @Test
    fun renderLoadingState() {
        runBlocking {
            stateSubject.emit(IngredientsViewState(status = IngredientsViewState.Status.LOADING))

            onView(withId(R.id.loading)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun renderNetworkError() {
        runBlocking {
            stateSubject.emit(IngredientsViewState(status = IngredientsViewState.Status.LOADING_ERROR))

            onView(withText(R.string.generic_network_error)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun renderEmptyState() {
        runBlocking {
            stateSubject.emit(IngredientsViewState(status = IngredientsViewState.Status.NO_RESULTS))

            onView(withText(R.string.empty_state_ingredients)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun swipeToRefresh() {
        runBlocking {
            stateSubject.emit(IngredientsViewState(listOfIngredients))
            onView(withId(R.id.swipeToRefresh)).perform(swipeDown())

            verify(viewController).accept(IngredientsViewEvent.ClickRefresh)
        }
    }

    @Test
    fun clickAddIngredient() {
        runBlocking {
            stateSubject.emit(IngredientsViewState())
            onView(withId(R.id.buttonAddIngredient)).perform(click())

            verify(viewController).accept(IngredientsViewEvent.ClickAddIngredient)
        }
    }

    @Test
    fun clickOnIngredient() {
        runBlocking {
            val argumentCaptor = argumentCaptor<IngredientsViewEvent>()
            stateSubject.emit(IngredientsViewState(listOfIngredients))
            onView(withId(R.id.ingredients))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<IngredientItemViewHolder>(
                        0,
                        click()
                    )
                )

            verify(viewController).accept(argumentCaptor.capture())
            assertEquals(
                listOfIngredients.first(),
                (argumentCaptor.firstValue as IngredientsViewEvent.IngredientClicked).ingredient
            )
        }
    }

    @Test
    fun search() {
        runBlocking {
            val argumentCaptor = argumentCaptor<IngredientsViewEvent>()

            stateSubject.emit(IngredientsViewState())
            onView(withId(R.id.searchBar)).perform(click())
            onView(withId(R.id.search_src_text)).perform(ViewActions.typeText(searchCharacter))

            verify(viewController).accept(argumentCaptor.capture())
            assertEquals(
                searchCharacter,
                (argumentCaptor.firstValue as IngredientsViewEvent.Search).name
            )
        }
    }

    @Test
    fun clickCloseSearch() {
        runBlocking {
            stateSubject.emit(IngredientsViewState())
            onView(withId(R.id.searchBar)).perform(click())
            onView(withId(R.id.search_close_btn)).perform(click())

            verify(viewController).accept(IngredientsViewEvent.ClickCloseSearch)
        }
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
