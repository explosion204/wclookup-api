package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ListResponse
import com.example.wclookup.core.model.Review
import retrofit2.Response
import retrofit2.http.*

interface WclookupReviewApi {
    @GET("/api/reviews")
    suspend fun getAll(
        @Header("Access-Token") accessToken: String,
        @Query("toiletId") toiletId: Long,
        @Query("page") page: Int,
        @Query("pageSize") size: Int,
    ): Response<ListResponse<Review>>

    @GET("/api/reviews/{id}")
    suspend fun getById(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long
    ): Response<Review>

    @POST("/api/reviews")
    suspend fun create(
        @Header("Access-Token") accessToken: String,
        @Body review: Review
    ): Response<Review>

    @PATCH("/api/reviews/{id}")
    suspend fun update(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long,
        @Body review: Review
    ): Response<Review>

    @DELETE("/api/reviews/{id}")
    suspend fun delete(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long
    ): Void
}