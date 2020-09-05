package com.astutify.mvi.demo

import android.app.Application

class App : Application(), AppComponentProvider {

    override val appComponent : AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }
}
