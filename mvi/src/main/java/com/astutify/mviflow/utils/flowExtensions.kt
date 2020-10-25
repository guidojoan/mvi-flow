package com.astutify.mviflow.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.startWithEvent(event: T) = this.onStart { emit(event) }

fun <Event> terminalEvent() = emptyFlow<Event>()
