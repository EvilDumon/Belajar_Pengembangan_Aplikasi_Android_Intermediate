package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.data.LoginPreferences
import com.example.mystoryapp.data.response.LoginResponse
import com.example.mystoryapp.data.retrofit.ApiConfig
import com.example.mystoryapp.ui.MainActivity.Companion.token
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: LoginPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed

    fun getTokenLogin(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            pref.clearToken()
        }
    }


    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    saveToken(response.body()?.loginResult?.token.toString())
                    _isFailed.value = response.body()?.error
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        const val TAG = "LoginViewModel"
    }
}
class LoginViewModelFactory (private val pref: LoginPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}