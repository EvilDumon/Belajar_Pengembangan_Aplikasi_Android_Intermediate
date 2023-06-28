package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.data.response.StoryResponse
import com.example.mystoryapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem>?>()
    val listStory: LiveData<List<ListStoryItem>?> = _listStory

    fun getStoriesWithLocation(authHeader: String) {
        val api = ApiConfig.getApiService().getStoriesWithLocation("Bearer $authHeader")
        api.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if ( response.isSuccessful) {
                    _listStory.value = response.body()?.listStory as List<ListStoryItem>
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    companion object{
        const val TAG = "MapsViewModel"
    }
}