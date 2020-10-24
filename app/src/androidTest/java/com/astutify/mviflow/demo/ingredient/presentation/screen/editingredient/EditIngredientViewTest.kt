package com.astutify.mviflow.demo.ingredient.presentation.screen.editingredient

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.astutify.mviflow.demo.R
import com.astutify.mviflow.demo.ingredient.MockApp
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientView
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewEvent
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewFeature
import com.astutify.mviflow.demo.presentation.screen.editingredient.EditIngredientViewState
import com.astutify.mviflow.demo.presentation.screen.editingredient.di.EditIngredientViewComponent
import com.astutify.mviflow.demo.utils.test.TestHelper
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

class EditIngredientViewTest {

    private val stateSubject = MutableStateFlow(EditIngredientViewState())
    private val viewController: EditIngredientViewFeature = mock {
        on { connect() } doReturn stateSubject
    }

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<EditIngredientView> =
        object : ActivityTestRule<EditIngredientView>(EditIngredientView::class.java) {

            override fun beforeActivityLaunched() {
                ApplicationProvider.getApplicationContext<MockApp>().appComponent = mock {
                    on { editIngredientViewBuilder() } doReturn EditIngredientViewComponentBuilder(
                        viewController
                    )
                }
            }
        }

    @Test
    fun renderIngredientName() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(ingredient))

            onView(withId(R.id.ingredientName)).check(matches(withText(ingredient.name)))
        }
    }

    @Test
    fun renderEditIngredientTitle() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(mode = EditIngredientViewState.Mode.EDIT))

            onView(withText(R.string.edit_ingredient)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun renderAddIngredientTitle() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(mode = EditIngredientViewState.Mode.NEW))

            onView(withText(R.string.add_ingredient)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun disableSaveButton() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(saveEnabled = false))

            onView(withId(R.id.saveButton)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun enableSaveButton() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(saveEnabled = true))

            onView(withId(R.id.saveButton)).check(matches((isEnabled())))
        }
    }

    @Test
    fun renderLoadingState() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(status = EditIngredientViewState.Status.LOADING))

            onView(withId(R.id.loadingProgress)).check(matches(isDisplayed()))
            onView(withId(R.id.ingredientName)).check(matches(not(isEnabled())))
        }
    }

    @Test
    fun renderEditState() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(status = null))

            onView(withId(R.id.loadingProgress)).check(matches(not(isDisplayed())))
            onView(withId(R.id.ingredientName)).check(matches(isEnabled()))
        }
    }

    @Test
    fun renderErrorOnSaveState() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(status = EditIngredientViewState.Status.ERROR_SAVE))

            onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.generic_network_error)))
        }
    }

    @Test
    fun clickBack() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState())
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

            verify(viewController).accept(EditIngredientViewEvent.ClickBack)
        }
    }

    @Test
    fun clickSave() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState(saveEnabled = true))
            onView(withId(R.id.saveButton)).perform(click())

            verify(viewController).accept(EditIngredientViewEvent.Save)
        }
    }

    @Test
    fun writeIngredientName() {
        runBlocking {
            stateSubject.emit(EditIngredientViewState())
            onView(withId(R.id.ingredientName)).perform(click()).perform(replaceText(ingredientName))

            verify(viewController).accept(EditIngredientViewEvent.NameChange(ingredientName))
        }
    }

    companion object {
        private const val ingredientName = "Tomato"
        private val ingredient = TestHelper.getIngredientVM()
    }
}

private class EditIngredientViewComponentBuilder(
    private val controller: EditIngredientViewFeature
) : EditIngredientViewComponent.Builder {

    override fun withActivity(view: AppCompatActivity): EditIngredientViewComponent.Builder =
        this

    override fun withIngredient(view: IngredientViewModel?): EditIngredientViewComponent.Builder =
        this

    override fun withInitialState(state: EditIngredientViewState?): EditIngredientViewComponent.Builder =
        this

    override fun build(): EditIngredientViewComponent =
        object : EditIngredientViewComponent {

            override fun inject(view: EditIngredientView) {
                view.controller = controller
            }
        }
}
