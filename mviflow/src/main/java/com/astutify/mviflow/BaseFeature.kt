package com.astutify.mviflow

import android.os.Bundle
import android.os.Parcelable
import kotlinx.coroutines.flow.Flow

interface BaseFeature<State : Parcelable, Event : Any, Effect : Any> {
    fun connect() : Flow<State>
    fun accept(event: Event)
    fun saveState(outState: Bundle)
}