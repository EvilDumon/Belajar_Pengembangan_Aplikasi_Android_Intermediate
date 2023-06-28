package com.example.mystoryapp.data.retrofit

import com.example.mystoryapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header ("Authorization") authHeader : String
    ): StoryResponse

    @GET("stories?location=1")
    fun getStoriesWithLocation(
        @Header ("Authorization") authHeader : String
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String,
        @Header ("Authorization") authHeader : String
    ) : Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header ("Authorization") authHeader : String
    ) : Call<PostResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>
}