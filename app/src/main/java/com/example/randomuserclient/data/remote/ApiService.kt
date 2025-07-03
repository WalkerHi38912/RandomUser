package com.example.randomuserclient.data.remote

import com.example.randomuserclient.data.Entitys.UserResponse
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun getUsersList(
        @retrofit2.http.Query("results") result: Int = 50
    ): UserResponse
}