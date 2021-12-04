package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Toilet
import retrofit2.http.*

interface WclookupToiletApi {
    @GET("/api/toilets")
    suspend fun getAll(
        @Header("accessToken") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): ApiResponse<Toilet>

    @GET("/api/toilets/{id}")
    suspend fun getById(
        @Header("accessToken") accessToken: String,
        @Path("id") id: Long
    ): ApiResponse<Toilet>

    @POST("/api/toilets")
    suspend fun create(
        @Header("accessToken") accessToken: String,
        @Body toilet: Toilet
    ): ApiResponse<Toilet>
}