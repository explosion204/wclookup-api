package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ListResponse
import com.example.wclookup.core.model.Toilet
import retrofit2.Response
import retrofit2.http.*

interface WclookupToiletApi {
    @GET("/api/toilets")
    suspend fun getAll(
        @Header("Access-Token") accessToken: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): Response<ListResponse<Toilet>>

    @GET("/api/toilets/{id}")
    suspend fun getById(
        @Header("Access-Token") accessToken: String,
        @Path("id") id: Long
    ): Response<Toilet>

    @POST("/api/toilets")
    suspend fun create(
        @Header("Access-Token") accessToken: String,
        @Body toilet: Toilet
    ): Response<Toilet>
}