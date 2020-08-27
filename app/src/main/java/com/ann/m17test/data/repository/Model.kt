package com.ann.m17test.data.repository

import com.ann.m17test.data.model.User
import com.ann.m17test.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Model {
    suspend fun getSearchResultStream(queryString: String): Flow<Resource<List<User>>>
    suspend fun requestMore(immutableQuery: String)
}