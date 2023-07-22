package com.example.smilie.model.service

import com.example.smilie.model.User
import com.example.smilie.model.Metric
import com.example.smilie.model.Value
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/user/{id}")
    suspend fun get(@Path("id") id: String): User?
}

interface MetricService {
    @Headers(
        "Accept: application/json"
    )
    @GET("smilie-90a63/us-central1/api/user/{id}/metrics")
    suspend fun getAll(@Path("id") id: String): ArrayList<Metric>

    @PUT("smilie-90a63/us-central1/api/user/{id}/metrics")
    suspend fun put(@Path("id") id: String, @Body metrics: ArrayList<Metric>): ArrayList<Metric>

    @PUT("smilie-90a63/us-central1/api/user/{id}/metrics/{metric_id}/values")
    suspend fun put(@Path("id") id: String, @Path("metric_id") metric_id: String, @Body value: Value): Value
}