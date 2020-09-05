package com.astutify.mvi.demo

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class AppModule {

    @Provides
    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler {
        return Schedulers.newThread()
    }

    @Provides
    @Named("ui_thread")
    fun provideUiThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
