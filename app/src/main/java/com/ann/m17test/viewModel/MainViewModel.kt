package com.ann.m17test.viewModel

import androidx.lifecycle.*
import com.ann.m17test.data.model.User
import com.ann.m17test.data.repository.Model
import com.ann.m17test.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel(), KoinComponent {
    private val mainRepository: Model by inject()

    val queryLiveData = MutableLiveData<String>()
    val users: LiveData<Resource<List<User>>> = queryLiveData.switchMap { queryString: String ->
        liveData {
            val repos =
                mainRepository.getSearchResultStream(queryString).asLiveData()
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