package com.ann.m17test.data.api

class ApiHelperImpl(private val apiService: ApiService) :
    ApiHelper {
    override suspend fun getUsersByPaging(q: String, page: Int, perPage: Int) =
        apiService.getUsersByPaging(q, page, perPage)

}