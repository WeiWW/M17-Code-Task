package com.ann.m17test.data.repository

import android.util.Log
import com.ann.m17test.data.api.ApiHelper
import com.ann.m17test.data.model.User
import com.ann.m17test.data.model.UserSearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 100

@ExperimentalCoroutinesApi
class MainRepository(private val apiHelper: ApiHelper) {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = GITHUB_STARTING_PAGE_INDEX

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<User>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    private var isReachTotalCount = false

    // keep channel of results. The channel allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = ConflatedBroadcastChannel<UserSearchResult>()

    suspend fun getUsers(q: String) = apiHelper.getUsers(q)


    suspend fun getSearchResultStream(queryString: String): Flow<UserSearchResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        isReachTotalCount = false
        requestAndSaveData(queryString)
        return searchResults.asFlow()
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false

        try {
            if(!isReachTotalCount){
                val response = apiHelper.getUsersByPaging(query, lastRequestedPage, NETWORK_PAGE_SIZE)
                Log.d("REPO", "$response")
                inMemoryCache.addAll(response.items)
                Log.d("REPO", "cache total: ${inMemoryCache.size}")
                isReachTotalCount = inMemoryCache.size == response.total_count
                searchResults.offer(UserSearchResult.Success(inMemoryCache))
                successful = true
            }else{
                successful = false
                Log.d("REPO", "total: ${inMemoryCache.size}")
            }

        } catch (exception: IOException) {
            searchResults.offer(UserSearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.offer(UserSearchResult.Error(exception))
        }

        isRequestInProgress = false
        return successful

    }

    suspend fun requestMore(immutableQuery: String) {
        if (isRequestInProgress || isReachTotalCount) return
        val successful = requestAndSaveData(immutableQuery)
        if (successful) {
            lastRequestedPage++
        }
    }
}