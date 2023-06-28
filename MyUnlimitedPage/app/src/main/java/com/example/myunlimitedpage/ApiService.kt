package com.example.myunlimitedpage

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("users")
    fun getUsers(
        @QueryMap parameters: HashMap<String, String>
    ): Call<UsersResponse>
}