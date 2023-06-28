package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.database.StoryDatabase
import com.example.mystoryapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}