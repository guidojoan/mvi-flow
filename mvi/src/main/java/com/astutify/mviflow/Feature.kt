package com.astutify.mviflow

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class Feature<State : Parcelable, Event : Any, Effect : Any>(
    initialState: State,
    private val effectHandler: EffectHandler<State, Event, Effect>,
    private val reducer: Reducer<State, Event, Effect>
) : BaseFeature<State, Event, Effect>, LifecycleEventObserver {

    private val eventSubject = Channel<Event>(Channel.UNLIMITED)

    private val stateSubject = MutableStateFlow(initialState)

    private var scope: CoroutineScope? = null

    val state: State
        get() = stateSubject.value

    abstract fun initialEvent(): Event?

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            initFeature(source)
        }
    }

    private fun initFeature(source: LifecycleOwner) {
        if (scope == null) {
            scope = source.lifecycleScope
            startListeningForEvents(source.lifecycleScope)
        }
    }

    private fun startListeningForEvents(scope: LifecycleCoroutineScope){
        scope.launch {
            eventSubject.consumeAsFlow()
                .collect { event ->
                    invokeReducer(state, event)
                }
        }
    }

    private suspend fun invokeReducer(state: State, event: Event) {
        reducer(state, event).apply {
            var reducedState = state

            this.state?.let {
                reducedState = it
                stateSubject.emit(it)
            }

            this.effect?.let {
                invokeEffectHandler(reducedState, it)
            }
        }
    }

    private fun invokeEffectHandler(state: State, effect: Effect) {
        scope?.launch {
            effectHandler
                .invoke(state, effect)
                .collect { event ->
                    invokeReducer(stateSubject.value, event)
                }
        }
    }

    override fun connect(): Flow<State> {
        initialEvent()?.let {
            eventSubject.offer(it)
        }
        return stateSubject
    }

    override fun saveState(outState: Bundle) {
        outState.putParcelable(FEATURE_SAVED_STATE, state)
    }

    override fun accept(event: Event) {
        eventSubject.offer(event)
    }

    companion object {
        const val FEATURE_SAVED_STATE = "featureSavedState"
    }
}
