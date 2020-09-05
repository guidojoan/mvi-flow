package com.astutify.mvi

sealed class ReducerResult<State, Effect>(
    val state: State? = null,
    val effect: Effect? = null
)

class Effect<State, Effect>(effect: Effect) : ReducerResult<State, Effect>(effect = effect)
class State<State, Effect>(state: State) : ReducerResult<State, Effect>(state = state)
class Next<State, Effect>(state: State, effect: Effect) :
    ReducerResult<State, Effect>(state = state, effect = effect)
