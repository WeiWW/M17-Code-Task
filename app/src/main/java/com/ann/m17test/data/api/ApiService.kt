package com.ann.m17test.data.api

import com.ann.m17test.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(@Query("q") q: String): Response<List<User>>
}