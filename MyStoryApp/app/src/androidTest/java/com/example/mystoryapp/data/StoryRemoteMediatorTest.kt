package com.example.mystoryapp.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mystoryapp.data.database.StoryDatabase
import com.example.mystoryapp.data.response.*
import com.example.mystoryapp.data.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}
class FakeApiService : ApiService {
    override suspend fun getStories(page: Int, size: Int, authHeader: String): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                    "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1683497415212_dnKjbXF-.jpg",
                "2023-05-07T22:10:15.213Z",
                    "Andre",
                "Halo",
                37.422092,
                -122.08392)
            items.add(story)
        }
        val storyResponse = StoryResponse(
            items,
            false,
            "Stories fetched successfully"
        )
        return storyResponse
    }

    override fun getStoriesWithLocation(authHeader: String): Call<StoryResponse> {
        TODO("Not yet implemented")
    }

    override fun getDetailStory(id: String, authHeader: String): Call<DetailStoryResponse> {
        TODO("Not yet implemented")
    }

    override fun postStory(
        file: MultipartBody.Part,
        description: RequestBody,
        authHeader: String
    ): Call<PostResponse> {
        TODO("Not yet implemented")
    }

    override fun postLogin(email: String, password: String): Call<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun postRegister(
        name: String,
        email: String,
        password: String
    ): Call<RegisterResponse> {
        TODO("Not yet implemented")
    }
}