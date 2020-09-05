package com.astutify.mvi.demo.ingredient.presentation.screen.editingredient

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.TestHelper
import com.astutify.mvi.demo.ingredient.MockApp
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientView
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewEvent
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewFeature
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientViewState
import com.astutify.mvi.demo.presentation.screen.editingredient.di.EditIngredientViewComponent
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.mockito.stubbing.Answer

class EditIngredientViewTest {

    private val stateSubject = BehaviorSubject.create<EditIngredientViewState>()
    private val viewController: EditIngredientViewFeature = mock {
        on { subscribe(any()) } doAnswer (
                Answer {
                    val observer = it.arguments[0] as Observer<EditIngredientViewState>
                    stateSubject.subscribe(observer)
                }
                )
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

    private fun runOnUiThread(runnable: () -> Unit) {
        activityRule.runOnUiThread(runnable)
    }

    @Test
    fun renderIngredientName() {
        runOnUiThread {
            stateSubject.onNext(
                EditIngredientViewState(
                    ingredient
                )
            )
        }

        onView(withId(R.id.ingredientName)).check(matches(withText(ingredient.name)))
    }

    @Test
    fun renderEditIngredientTitle(){
        runOnUiThread {
            stateSubject.onNext(
                EditIngredientViewState(
                    mode = EditIngredientViewState.Mode.EDIT
                )
            )
        }

        onView(withText(R.string.edit_ingredient)).check(matches(isDisplayed()))
        onView(withId(R.id.search_src_text)).perform()
    }

    @Test
    fun renderAddIngredientTitle(){
        runOnUiThread {
            stateSubject.onNext(
                EditIngredientViewState(
                    mode = EditIngredientViewState.Mode.NEW
                )
            )
        }

        onView(withText(R.string.add_ingredient)).check(matches(isDisplayed()))
    }

    @Test
    fun disableSaveButton(){
        runOnUiThread {
            stateSubject.onNext(
                EditIngredientViewState(
                    saveEnabled = false
                )
            )
        }

        onView(withId(R.id.saveButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun enableSaveButton(){
        runOnUiThread {
            stateSubject.onNext(
                EditIngredientViewState(
                    saveEnabled = true
                )
            )
        }

        onView(withId(R.id.saveButton)).check(matches((isEnabled())))
    }

    @Test
    fun showLoading() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState(status = EditIngredientViewState.Status.LOADING))
        }

        onView(withId(R.id.loadingProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredientName)).check(matches(not(isEnabled())))
    }

    @Test
    fun hideLoading() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState(status = null))
        }

        onView(withId(R.id.loadingProgress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredientName)).check(matches(isEnabled()))
    }

    @Test
    fun showErrorSave() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState(status = EditIngredientViewState.Status.ERROR_SAVE))
        }

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.generic_network_error)))
    }

    @Test
    fun clickBack() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState())
        }

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

        verify(viewController).accept(EditIngredientViewEvent.ClickBack)
    }

    @Test
    fun clickSave() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState(saveEnabled = true))
        }

        onView(withId(R.id.saveButton)).perform(click())

        verify(viewController).accept(EditIngredientViewEvent.Save)
    }

    @Test
    fun writeIngredientName() {
        runOnUiThread {
            stateSubject.onNext(EditIngredientViewState())
        }

        onView(withId(R.id.ingredientName)).perform(click())
            .perform(typeText(ingredientName))

        verify(viewController).accept(EditIngredientViewEvent.NameChange(ingredientName))
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
