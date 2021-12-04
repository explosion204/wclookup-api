package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.model.User
import retrofit2.Response
import retrofit2.http.*

interface WclookupUserApi {
    @GET("/api/users/{id}")
    suspend fun getById(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long
    ): Response<ApiResponse<User>>

    @PATCH("/api/users/{id}")
    suspend fun update(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long,
        @Body user: User
    ): Response<ApiResponse<User>>
}