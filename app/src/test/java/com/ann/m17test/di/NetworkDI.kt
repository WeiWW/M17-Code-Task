package com.ann.m17test.di

import com.ann.m17test.data.api.ApiService
import com.ann.m17test.utils.ConnectedNetworkHelper
import com.ann.m17test.utils.NetworkHelper
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun networkTestModule(url: String) = module {
    single { provideRetrofit(url) }
    single { provideNetworkHelper() }
    factory { get<Retrofit>().create(ApiService::class.java) }
}

private fun provideNetworkHelper(): NetworkHelper = ConnectedNetworkHelper()
private fun provideRetrofit(url: String): Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(url)
    .build()