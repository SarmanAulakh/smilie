package com.example.smilie.model.service

import com.example.smilie.model.User
import com.example.smilie.model.Metric
import com.example.smilie.model.Value
import com.example.smilie.screens.settings.UserUpdateBody
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/users/{id}")
    suspend fun get(@Path("id") id: String): User?

    @Headers(
        "Accept: application/json"
    )
    @POST("smilie-90a63/us-central1/api/users")
    suspend fun create(@Body user: User): User?

    @Headers(
        "Accept: application/json"
    )
    @PUT("smilie-90a63/us-central1/api/users/{id}")
    suspend fun put(@Path("id") id: String, @Body userUpdateBody: UserUpdateBody): User?

    @GET("smilie-90a63/us-central1/api/users")
    suspend fun getAll(): ArrayList<User>?
}

interface MetricService {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/users/{id}/metrics")
    suspend fun getAll(@Path("id") id: String): ArrayList<Metric>
    @PUT("smilie-90a63/us-central1/api/users/{id}/metrics")
    suspend fun put(@Path("id") id: String, @Body metrics: ArrayList<Metric>): Boolean
    @PUT("smilie-90a63/us-central1/api/users/{id}/metrics/{metric_id}")
    suspend fun put(@Path("id") id: String, @Path("metric_id") metric_id: String, @Body value: Value): Boolean

}