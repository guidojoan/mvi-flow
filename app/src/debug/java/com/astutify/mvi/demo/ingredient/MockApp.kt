package com.astutify.mvi.demo.ingredient

import android.app.Application
import com.astutify.mvi.demo.AppComponent
import com.astutify.mvi.demo.AppComponentProvider

class MockApp : Application(), AppComponentProvider {
    override lateinit var appComponent : AppComponent
}
