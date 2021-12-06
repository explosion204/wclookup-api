package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ListResponse
import com.example.wclookup.core.model.User
import retrofit2.Response
import retrofit2.http.*

interface WclookupUserApi {
    @GET("/api/users/{id}")
    suspend fun getById(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long
    ): Response<User>

    @PATCH("/api/users/{id}")
    suspend fun update(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long,
        @Body user: User
    ): Response<User>
}