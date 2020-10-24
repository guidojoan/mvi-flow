package com.astutify.mviflow.demo

import android.app.Application
import com.astutify.mviflow.demo.di.AppComponent
import com.astutify.mviflow.demo.di.AppComponentProvider
import com.astutify.mviflow.demo.di.DaggerAppComponent

class App : Application(), AppComponentProvider {

    override val appComponent : AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .build()
    }
}
