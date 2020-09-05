## What is MVI architecture?

This is a library to create screens following the MVI architecture. To understand this architecture you have a plenty of articles. I recommend you to read the following links to understand this architecture.

http://hannesdorfmann.com/android/mosby3-mvi-1

https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started

## Core concepts about MVI library

To know how to use it you need to understand the following concepts:



#### **State**

Represents a particular **State** of the **User Interface**



#### **Event**

Represents that something has happened.

- *Triggered from the* **UI** -> Represents an user interaction
- *Triggered from the* **Effect Handler** -> Represents that some work finished.

This tells to the **Reducer** that something happened and that it needs to create a new **State** in response to the **Event**.



#### **Effect**

Represents an side-effect from an **Event**. It is triggered from the Reducer when:

- there is not enough information to generate the new **State**
- there is no need of a new **State** but some extra work must be done



#### **Effect Handler**

The effect handler is used when you need to interact with others to provide the new state to the screen. For example: get data from the backend. Normally this operations occurs on background thread and finishes emitting a new Event.

Other use of the effect handler is when you don't need to update the UI emitting a new state. For example: send events to analytics, navigate to another screen. Normally this operations does not emit any new state.

Effect Handler receives an Effect, executes the task needed and then returns another Event (that will be processed by the Reducer) or not emit anything. The Effect handler receives the data from the State in case you need to use that information.

Effect Handler must not throw exceptions. Instead you must return an Event that represents the error. Then the Reducer will choose how to process that Error.



#### **Reducer**

The Reducer receives Events from the UI and generates a new State.

If the Reducer has all information to return a new State to the UI it generates the new state and then returns it.

If the Reducer needs more information to generate the new State it returns an Effect to be processed by the Effect Handler.

In some cases you need to update the State and do other tasks (send events to analytics, get some data from the backend). To do both at the same time the reducer can return Next object that includes a new State and an Effect. In this special case the Feature first will update the State and then it will invoke the Effect Handler passing the updated State and the Effect.

If we join all of the components of the MVI library...

![](https://astutify.com/library/mvi/mvi_components.png)

## How to use the MVI library.



#### Define the State, Events and Effects

The following example is a screen that renders a list of ingredients. 

It also represents on a different way the following scenarios: 

- loading
- loading error
- no results

```kotlin
@Parcelize
data class IngredientsViewState(
    val ingredients: List<IngredientViewModel> = emptyList(),
    val status: Status? = null
) : Parcelable {

    enum class Status {
        LOADING, LOADING_ERROR, NO_RESULTS
    }
}
```

Events: This represents the different events that can be triggered by the user or by the effect handler.

```kotlin
sealed class IngredientsViewEvent {
    object Loading : IngredientsViewEvent()
    object LoadData : IngredientsViewEvent()
    object LoadingError : IngredientsViewEvent()
    class DataLoaded(val ingredients: List<IngredientViewModel>) : IngredientsViewEvent()
}
```

Effect: The different effects returned by the reducer when there is not enough information to create a new state. In this example the reducer does not have the list of ingredients, so it emits an effect.

```kotlin
sealed class IngredientsViewEffect {
    object LoadData : IngredientsViewEffect()
}
```



#### Implement the Reducer

```kotlin
class IngredientsViewReducer :
    Reducer<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect>() {

    override fun invoke(
        state: IngredientsViewState,
        event: IngredientsViewEvent
    ): ReducerResult<IngredientsViewState, IngredientsViewEffect> {
        return when (event) {
            is IngredientsViewEvent.LoadData -> Effect(IngredientsViewEffect.LoadData)
            is IngredientsViewEvent.Loading -> State(state.copyState(status = IngredientsViewState.Status.LOADING))
            is IngredientsViewEvent.DataLoaded -> State(
                state.copyState(
                    status = if (event.ingredients.isEmpty()) IngredientsViewState.Status.NO_RESULTS else null,
                    ingredients = event.ingredients
                )
            )
            is IngredientsViewEvent.LoadingError -> State(state.copyState(status = IngredientsViewState.Status.LOADING_ERROR))
        }
    }
}
```



#### Implement the Effect Handler

```kotlin
class IngredientsViewEffectHandler constructor(
    private val main: Scheduler,
    private val getIngredientsUseCase: GetIngredientsUseCase
) : EffectHandler<IngredientsViewState, IngredientsViewEvent, IngredientsViewEffect> {

    override fun invoke(
        state: IngredientsViewState,
        effect: IngredientsViewEffect
    ): Flowable<out IngredientsViewEvent> {
        return when (effect) {
            is IngredientsViewEffect.LoadData -> {
                getIngredientsUseCase()
                    .observeOn(main)
                    .toFlowable()
                    .map<IngredientsViewEvent> { response ->
                        response.fold(
                            {
       							IngredientsViewEvent.DataLoaded(it.map {toPresentation(it)})
                            },
                            {
                                IngredientsViewEvent.LoadingError
                            }
                        )
                    }
                    .startWith(IngredientsViewEvent.Loading)
            }
        }
    }
}
```



#### Implement the FeatureController

- **initialState** : This is the initial state that you want to show on screen to the user when the Screen is instantiated. 

- **initialEvent()**: In some cases you need to start the Screen with some Event that is not triggered by the user before to let the user interact with the UI. For example: load data from the backend when the user enters the Screen. 

  If you don't need to do anything before the user interaction you must return null on this function.



#### Create the screen

It can be an Activity, Fragment or custom view.

- Subscribe to the *FeatureController* to listen for the different *States* (The screen must have the logic to render the different states on window)

```kotlin
private fun render(viewState: IngredientsViewState) {
    view.ingredients.render(viewState.ingredients)
    
    when (viewState.status) {
        IngredientsViewState.Status.LOADING -> showLoading()
        IngredientsViewState.Status.LOADING_ERROR -> renderNetworkError()
        IngredientsViewState.Status.NO_RESULTS -> renderEmptyState()
        else -> hideStatusView()
    }
}
```

- Send *Events* to the *FeatureController* when something happened on the Screen (User interaction)

```kotlin
private fun initViews() {
    view.buttonAddIngredient.setOnClickListener {
        controller.accept(IngredientsViewEvent.ClickAddIngredient)
    }
    view.ingredients.bind {
        when (it) {
            is IngredientsListView.Event.IngredientClicked -> controller.accept(
                IngredientsViewEvent.IngredientClicked(it.ingredient)
            )
        }
    }
}
```

You have a full functional example app on this project with more complex logic.



## How to test an MVI screen

The classes generated using this MVI library are all unit testable with a 100% of testing coverage.  You have to test 3 kinds of classes:



#### Testing a Reducer

To unit test a reducer. For every Event you should:

1. Invoke the reducer with an Event and Initial State
2. Check that the reducer returns the new State of the Effect that it should return.



For example on our demo app:

To test an Event that should return a new State

```kotlin
@Test
fun `should return Loading State when invoked with LoadingSave Event`() {
    val expectedState = initialState.copyState(status = EditIngredientViewState.Status.LOADING)

    val result = reducer(initialState, EditIngredientViewEvent.LoadingSave)

    assertEquals(expectedState, result.state)
}
```

To test an Event that should return an Effect:

```kotlin
@Test
fun `should return GoToAddIngredient Effect when is invoked with ClickAddIngredient Event`() {
    val result = reducer.invoke(initialState, IngredientsViewEvent.ClickAddIngredient)

    assert(result.effect is IngredientsViewEffect.GoToAddIngredient)
}
```



#### Testing an Effect Handler

To unit test an Effect Handler. For every Effect you should:

1. Invoke the Effect Handler with the Effect under test and an initial State
2. Verify that the Effect Handler returns the expected Event/s or that not returns any Event
3. Verify that the Effect Handler do what it need to do (invoke a Use Case, send an analytics event, navigate to another screen )



For example on our demo app

To test an Effect that must navigate to some screen and not return an Event

```kotlin
@Test
fun `should navigate to add ingredient when invoked with GoToAddIngredient Effect`(){
    effectHandler.invoke(initialState,
        IngredientsViewEffect.GoToAddIngredient
    )
        .test()
        .assertNoErrors()
        .assertNoValues()

    verify(navigator).goToAddIngredient()
}
```

To test an Effect that must executes an Use Case and return the result in an Event

```kotlin
@Test
fun `should return Event with Ingredients when invoked with LoadData Effect`() {
    whenever(getIngredientsUseCase()).thenReturn(Single.just(successfulResponse))

    val result = effectHandler.invoke(initialState,IngredientsViewEffect.LoadData)
        .test()
        .values()

    assert(result[0] == IngredientsViewEvent.Loading)
    assert((result[1] as IngredientsViewEvent.DataLoaded).ingredients == listOfIngredientsVM)
    verify(getIngredientsUseCase).invoke()
}
```



#### Testing a View (Activity, Fragment, Custom View)

Testing the view is easy. You have to test two kind of scenarios:

1. Render a view state on screen
2. User interaction with screen that emits an event and sends it to view feature



##### Render a view state on screen

1. On the state subject emit the view state that you want to test
2. Check that the view is representing the view state on screen successfully



For example on our demo app:

```kotlin
@Test
fun renderIngredientName() {
    runOnUiThread {
        stateSubject.onNext(EditIngredientViewState(ingredient))
    }

    onView(withId(R.id.ingredientName)).check(matches(withText(ingredient.name)))
}
```



##### User interaction with screen

1. On the state subject emit one view state
2. With the espresso framework emulate the user interaction with the view
3. Check that the feature receives the event that represents the user interaction



For example on our demo app:

```kotlin
@Test
fun clickSave() {
    runOnUiThread {
        stateSubject.onNext(EditIngredientViewState(saveEnabled = true))
    }

    onView(withId(R.id.saveButton)).perform(click())

    verify(viewController).accept(EditIngredientViewEvent.Save)
}
```



## Setup the MVI library



##### With Gradle -> Jcenter

```kotlin
implementation 'com.astutify.mvi:core:1.0.0'
```