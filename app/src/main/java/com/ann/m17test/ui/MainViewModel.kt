package com.ann.m17test.ui

import androidx.lifecycle.*
import com.ann.m17test.data.model.GithubUser
import com.ann.m17test.data.model.UserSearchResult
import com.ann.m17test.data.repository.MainRepository
import com.ann.m17test.utils.NetworkHelper
import com.ann.m17test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val _users = MutableLiveData<Resource<GithubUser>>()
    val users: LiveData<UserSearchResult> = queryLiveData.switchMap { queryString: String ->
        liveData {
            val repos =
                mainRepository.getSearchResultStream(queryString).asLiveData(Dispatchers.Main)
            emitSource(repos)
        }
    }
    //get() = _users

    fun fetchUsers(query: String) {
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers(query).let {
                    if (it.isSuccessful) {
                        _users.postValue(Resource.success(it.body()))
                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemId: Int, totalItemCount: Int) {
        if (lastVisibleItemId + 3 >= totalItemCount) {
            val immutableQuery = queryLiveData.value
            if (immutableQuery != null) {
                viewModelScope.launch {
                    mainRepository.requestMore(immutableQuery)
                }
            }
        }
    }

    fun searchUser(search: String) {
        queryLiveData.postValue(search)
    }
}