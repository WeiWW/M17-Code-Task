package com.ann.m17test.ui.viewModel

import androidx.lifecycle.*
import com.ann.m17test.data.model.User
import com.ann.m17test.data.repository.MainRepository
import com.ann.m17test.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    val queryLiveData = MutableLiveData<String>()
    val users: LiveData<Resource<List<User>>> = queryLiveData.switchMap { queryString: String ->
        liveData {
            val repos =
                mainRepository.getSearchResultStream(queryString).asLiveData(Dispatchers.Main)
            emitSource(repos)
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
}