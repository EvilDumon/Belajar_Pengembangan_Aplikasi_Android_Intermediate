package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.response.DetailStoryResponse
import com.example.mystoryapp.data.response.Story
import com.example.mystoryapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailStory

    fun onStory(id: String, authHeader: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailStory(id, authHeader)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()?.story!!
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "StoryViewModel"
    }
}