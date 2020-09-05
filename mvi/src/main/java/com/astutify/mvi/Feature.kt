package com.astutify.mvi

import android.os.Bundle
import android.os.Parcelable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class Feature<State : Parcelable, Event : Any, Effect : Any>(
    initialState: State,
    effectHandler: EffectHandler<State, Event, Effect>,
    reducer: Reducer<State, Event, Effect>
) : Consumer<Event>, ObservableSource<State>, Disposable {

    private val disposables = DisposableCollection()
    private val eventSubject = PublishSubject.create<Event>()
    private val stateSubject = BehaviorSubject.createDefault(initialState)

    private val eventConsumer =
        EventConsumer(
            reducer,
            stateSubject,
            ::invokeEffectHandler
        )

    private val effectConsumer =
        EffectConsumer(
            disposables,
            effectHandler,
            stateSubject,
            ::invokeReducer
        )

    val state: State
        get() = stateSubject.value!!

    abstract fun initialEvent(): Event?

    init {
        disposables.add(effectConsumer)
        disposables.add(eventConsumer)
        disposables.add(
            eventSubject.subscribe {
                invokeReducer(state, it)
            }
        )
    }

    override fun subscribe(observer: Observer<in State>) {
        stateSubject.subscribe(observer)
        initialEvent()?.let {
            invokeReducer(state, it)
        }
    }

    fun saveState(outState: Bundle) {
        outState.putParcelable(FEATURE_SAVED_STATE, state)
    }

    override fun accept(event: Event) {
        eventSubject.onNext(event)
    }

    private fun invokeEffectHandler(state: State, effect: Effect) {
        if (isDisposed) return
        this.effectConsumer.accept(Pair(state, effect))
    }

    private fun invokeReducer(state: State, event: Event) {
        if (isDisposed) return
        eventConsumer.accept(Pair(state, event))
    }

    private class EffectConsumer<State : Any, Event : Any, Effect : Any>(
        private val disposables: DisposableCollection,
        private val effectHandler: EffectHandler<State, Event, Effect>,
        private val stateSubject: BehaviorSubject<State>,
        private val invokeReducer: (state: State, event: Event) -> Unit
    ) : Consumer<Pair<State, Effect>> {

        override fun accept(t: Pair<State, Effect>) {
            if (disposables.isDisposed) return
            val (state, action) = t

            disposables.add(
                effectHandler
                    .invoke(state, action)
                    .doOnNext { event ->
                        invokeReducer(stateSubject.value!!, event)
                    }
                    .subscribe()
            )
        }
    }

    private class EventConsumer<State : Any, Event : Any, Effect : Any>(
        private val reducer: Reducer<State, Event, Effect>,
        private val stateSubject: Subject<State>,
        private val invokeEffectHandler: (state: State, effect: Effect) -> Unit
    ) : Consumer<Pair<State, Event>> {

        override fun accept(t: Pair<State, Event>) {
            var (state, event) = t

            val reducerResult = reducer(state, event)
            reducerResult.state?.let {
                state = it
                stateSubject.onNext(it)
            }

            reducerResult.effect?.let {
                invokeEffectHandler(state, it)
            }
        }
    }

    override fun dispose() {
        disposables.dispose()
    }

    override fun isDisposed() =
        disposables.isDisposed

    companion object {
        const val FEATURE_SAVED_STATE = "featureSavedState"
    }
}
