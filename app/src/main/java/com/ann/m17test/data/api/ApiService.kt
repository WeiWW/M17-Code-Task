package com.ann.m17test.data.api

import com.ann.m17test.data.model.GithubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(@Query("q") q: String): Response<GithubUser>

    @GET("search/users")
    suspend fun getUsersByPaging(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): GithubUser
}