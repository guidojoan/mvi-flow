package com.astutify.mviflow

abstract class Reducer<State, Event, Effect> {

    abstract operator fun invoke(state: State, event: Event): ReducerResult<State, Effect>
}
