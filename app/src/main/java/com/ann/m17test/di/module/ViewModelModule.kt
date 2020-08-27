package com.ann.m17test.di.module

import com.ann.m17test.viewModel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel {
        MainViewModel()
    }
}