package com.astutify.mviflow.demo.ingredient

import android.app.Application
import com.astutify.mviflow.demo.di.AppComponent
import com.astutify.mviflow.demo.di.AppComponentProvider

class MockApp : Application(),
    AppComponentProvider {
    override lateinit var appComponent : AppComponent
}
