package com.example.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.data.database.StoryDatabase
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.data.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}