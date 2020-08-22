package com.ann.m17test

import android.app.Application
import com.ann.m17test.di.module.appModule
import com.ann.m17test.di.module.repoModule
import com.ann.m17test.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}