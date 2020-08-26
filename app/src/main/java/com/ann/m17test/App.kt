package com.ann.m17test

import android.app.Application
import com.ann.m17test.di.module.networkModule
import com.ann.m17test.di.module.repoModule
import com.ann.m17test.di.module.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf( networkModule,repoModule, viewModelModule))
        }
    }
}