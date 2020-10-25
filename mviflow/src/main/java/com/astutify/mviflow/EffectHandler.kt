package com.astutify.mviflow

import kotlinx.coroutines.flow.Flow

typealias EffectHandler<State, Event, Effect> = (state: State, effect: Effect) -> Flow<Event>
