package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.exception.RefreshTokenException
import com.example.wclookup.core.model.AuthResponse
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.service.AuthService
import com.google.gson.GsonBuilder
import java.util.*

class AuthServiceImpl : AuthService {
    override suspend fun authenticate(googleAccessToken: String): AuthResponse {
        val authResponse: AuthResponse = NetworkService.instance
            .getAuthApi()
            .authenticate(googleAccessToken)

        if (authResponse.errorMessage.isNotEmpty()) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        return gson.fromJson(gson.toJson(authResponse), AuthResponse::class.java)
    }

    override suspend fun refresh(refreshToken: String): AuthResponse {
        val authResponse: AuthResponse = NetworkService.instance
            .getAuthApi()
            .refresh(refreshToken)

        if (authResponse.errorMessage.isNotEmpty()) {
            throw RefreshTokenException()
        }

        val gson = GsonBuilder().create()
        return gson.fromJson(gson.toJson(authResponse), AuthResponse::class.java)
    }
}