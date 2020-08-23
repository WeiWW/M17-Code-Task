package com.ann.m17test.di.module

import com.ann.m17test.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val repoModule = module {
    single {
        MainRepository(get())
    }
}