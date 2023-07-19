package com.example.smilie.model.service

import com.example.smilie.model.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/user/{id}")
    suspend fun get(@Path("id") id: String): User?
}