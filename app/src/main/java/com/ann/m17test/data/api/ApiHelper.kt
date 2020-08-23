package com.ann.m17test.data.api

import com.ann.m17test.data.model.GithubUser
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(q: String): Response<GithubUser>

    suspend fun getUsersByPaging(q: String, page: Int, perPage: Int): GithubUser
}