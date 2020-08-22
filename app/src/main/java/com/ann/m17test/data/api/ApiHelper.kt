package com.ann.m17test.data.api

import com.ann.m17test.data.model.User
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(q: String): Response<List<User>>
}