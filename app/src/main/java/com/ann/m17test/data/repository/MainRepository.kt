package com.ann.m17test.data.repository

import com.ann.m17test.data.api.ApiHelper


class MainRepository (private val apiHelper: ApiHelper) {

    suspend fun getUsers(q: String) =  apiHelper.getUsers(q)

}