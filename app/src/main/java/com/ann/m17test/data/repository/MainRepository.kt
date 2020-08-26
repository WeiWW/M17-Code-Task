package com.ann.m17test.data.repository

import com.ann.m17test.data.api.ApiService
import com.ann.m17test.data.model.User
import com.ann.m17test.utils.NetworkHelper
import com.ann.m17test.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

private const val GITHUB_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 100

@ExperimentalCoroutinesApi
class MainRepository(private val apiService: ApiService, private val networkHelper: NetworkHelper) {
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = GITHUB_STARTING_PAGE_INDEX

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<User>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    // check if load all the data
    private var isReachTotalCount = false

    // keep channel of results. The channel allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = ConflatedBroadcastChannel<Resource<List<User>>>()

    suspend fun getSearchResultStream(queryString: String): Flow<Resource<List<User>>> {
        inMemoryCache.clear()
        isReachTotalCount = false
        requestAndSaveData(queryString)
        return searchResults.asFlow()
    }

    private suspend fun requestAndSaveData(query: String){
        isRequestInProgress = true

        //Check if get all users
        if (isReachTotalCount) {
            searchResults.offer(Resource.full(null, "Is full"))
            isRequestInProgress = false
            return
        }

        //Loading
        searchResults.offer(Resource.loading(emptyList()))

        //Check network
        if (!networkHelper.isNetworkConnected()){
            //No network
            searchResults.offer(Resource.error("NO NETWORK", null))
            isRequestInProgress = false
            return
        }

        val response =
            apiService.getUsersByPaging(query, lastRequestedPage, NETWORK_PAGE_SIZE)

        if (!response.isSuccessful){
            //Response error
            searchResults.offer(Resource.error(response.errorBody().toString(), null))
            isRequestInProgress = false
            return
        }

        response.body()?.let {
            if (it.items.isNotEmpty()) inMemoryCache.addAll(it.items)
            if (inMemoryCache.size >= it.total_count) isReachTotalCount = true

            searchResults.offer(Resource.success(inMemoryCache))
            lastRequestedPage++
        }

        isRequestInProgress = false
    }

    suspend fun requestMore(immutableQuery: String) {
        if (!isRequestInProgress) requestAndSaveData(immutableQuery)
    }
}