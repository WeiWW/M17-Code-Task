package com.ann.m17test.di.module

import com.ann.m17test.ui.MainViewModel
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel


val viewModelModule = module {
    viewModel {
        MainViewModel(get(),get())
    }
}