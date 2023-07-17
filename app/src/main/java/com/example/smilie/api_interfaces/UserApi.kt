package com.example.smilie.api_interfaces
import com.example.smilie.model.User
import retrofit2.http.Headers
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface UserApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/user/{id}")
    fun getUserById(@Path("id") id: String): Call<User?>?
}