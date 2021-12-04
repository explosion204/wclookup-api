package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Review
import retrofit2.http.*

interface WclookupReviewApi {
    @GET("/api/reviews")
    suspend fun getAll(
        @Header("accessToken") accessToken: String,
        @Query("toiletId") toiletId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<Review>

    @GET("/api/reviews/{id}")
    suspend fun getById(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long
    ): ApiResponse<Review>

    @POST("/api/reviews")
    suspend fun create(
        @Header("accessToken") accessToken: String,
        @Body review: Review
    ): ApiResponse<Review>

    @PATCH("/api/reviews/{id}")
    suspend fun update(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long,
        @Body review: Review
    ): ApiResponse<Review>

    @DELETE("/api/reviews/{id}")
    suspend fun delete(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long
    ): ApiResponse<Review>
}