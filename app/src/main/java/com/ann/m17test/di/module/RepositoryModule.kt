package com.ann.m17test.di.module

import com.ann.m17test.data.repository.MainRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepository(get())
    }
}