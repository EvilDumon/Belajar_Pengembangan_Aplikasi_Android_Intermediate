package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.response.PostResponse
import com.example.mystoryapp.data.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed

    fun postStory(imageMultipart: MultipartBody.Part, description: String, authHeader: String) {
        _isLoading.value = true
        val desc = description.toRequestBody("text/plain".toMediaType())
        val client = ApiConfig.getApiService().postStory(imageMultipart, desc, authHeader)
        client.enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isFailed.value = response.body()?.error
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "PostViewModel"
    }
}