package com.ann.m17test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ann.m17test.data.model.User
import com.ann.m17test.data.repository.MainRepository
import com.ann.m17test.di.networkTestModule
import com.ann.m17test.utils.*
import com.ann.m17test.viewModel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito


@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class MainViewModelTest : KoinTest, MockSeverBase() {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineScopeRule()

    private val flow = flow {
        emit(Resource.success(emptyList<User>()))
    }

    private lateinit var repository: MainRepository

    @Before
    override fun setup() {
        super.setup()
        startKoin { modules(listOf(networkTestModule(getUrl()))) }
        repository = Mockito.mock(MainRepository::class.java)
    }

    @After
    fun cleanUp() {
        super.tearDown()
        stopKoin()
        coroutineRule.coroutineContext.cancel()
        coroutineRule.cleanupTestCoroutines()
    }

    @Test
    fun `MainViewModel receive data from repo successfully`() =
        coroutineRule.dispatcher.runBlockingTest {
            Mockito.`when`(repository.getSearchResultStream("")).thenReturn(flow)

            val viewModel = MainViewModel(repository)
            viewModel.queryLiveData.postValue("")
            val result = LiveDataTestUtil.getValue(viewModel.users)
            result?.status.shouldBeEqualTo(Status.SUCCESS)
        }
}

