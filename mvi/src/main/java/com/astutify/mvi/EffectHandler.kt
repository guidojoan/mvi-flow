package com.astutify.mvi

import io.reactivex.Flowable

typealias EffectHandler<State, Event, Effect> = (state: State, effect: Effect) -> Flowable<out Event>
