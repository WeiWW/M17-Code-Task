package com.ann.m17test.data.api

import com.ann.m17test.data.model.User
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) :
    ApiHelper {

    override suspend fun getUsers(q: String): Response<List<User>> = apiService.getUsers(q)

}