package com.ann.m17test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.ann.m17test.data.api.ApiService
import com.ann.m17test.data.model.User
import com.ann.m17test.data.repository.MainRepository
import com.ann.m17test.di.networkTestModule
import com.ann.m17test.utils.*
import kotlinx.coroutines.*
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class WebTest : KoinTest, MockSeverBase() {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    private val apiService: ApiService by inject()
    private val networkHelper: NetworkHelper by inject()


    @Before
    override fun setup() {
        super.setup()
        startKoin { modules(listOf(networkTestModule(getUrl()))) }
    }

    @After
    override fun tearDown() {
        super.tearDown()
        stopKoin()
    }

    @Test
    fun `server response error`() = runBlocking {
        val repo = MainRepository(apiService, networkHelper)

        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_NOT_FOUND,
                "response_error.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repo.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.status.shouldBe(Status.ERROR)
            }
        }
        yield()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `server response success`() = runBlocking {

        val repo = MainRepository(apiService, networkHelper)
        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_OK,
                "response_success.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repo.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.status.shouldBe(Status.SUCCESS)
            }
        }
        yield()
    }

    @Test
    fun `it is get end`() = runBlocking {
        val repo = MainRepository(apiService, networkHelper)
        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_OK,
                "response_empty_items.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repo.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.data?.size.shouldBe(0)
            }
        }
        yield()
    }
}