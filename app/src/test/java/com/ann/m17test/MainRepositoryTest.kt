package com.ann.m17test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.ann.m17test.data.model.User
import com.ann.m17test.data.repository.Model
import com.ann.m17test.di.module.repoModule
import com.ann.m17test.di.networkTestModule
import com.ann.m17test.utils.*
import com.ann.m17test.utils.SyncTaskExecutorRule
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
class MainRepositoryTest : KoinTest, MockSeverBase() {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineScopeRule()

    private val repository: Model by inject()

    @Before
    override fun setup() {
        super.setup()
        startKoin { modules(listOf(repoModule, networkTestModule(getUrl()))) }
    }

    @After
    override fun tearDown() {
        super.tearDown()
        stopKoin()
        coroutineRule.coroutineContext.cancel()
        coroutineRule.cleanupTestCoroutines()
    }

    @Test
    fun `server response error`() = runBlocking {
        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_NOT_FOUND,
                "response_error.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repository.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.status.shouldBe(Status.ERROR)
            }
        }
        yield()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `server response success`() = runBlocking {
        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_OK,
                "response_success.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repository.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.status.shouldBe(Status.SUCCESS)
            }
        }
        yield()
    }

    @Test
    fun `it is get end`() = runBlocking {
        enqueue(
            `mock network response with json file`(
                HttpURLConnection.HTTP_OK,
                "response_empty_items.json"
            )
        )

        launch(Dispatchers.Default) {
            val liveData = repository.getSearchResultStream("").asLiveData()
            liveData.observeForever { result: Resource<List<User>> ->
                result.data?.size.shouldBe(0)
            }
        }
        yield()
    }
}