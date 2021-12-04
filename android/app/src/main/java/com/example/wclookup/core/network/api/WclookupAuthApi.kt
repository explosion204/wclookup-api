package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.AuthResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response

interface WclookupAuthApi {
    @POST("/api/auth/authenticate")
    suspend fun authenticate(
        @Header("googleAccessToken") googleAccessToken: String
    ): Response<AuthResponse>

    @POST("/api/auth/refresh")
    suspend fun refresh(
        @Header("refreshToken") refreshToken: String
    ): Response<AuthResponse>
}