package com.example.wclookup.core.service

import com.example.wclookup.core.model.AuthResponse


interface AuthService {
    suspend fun authenticate(googleAccessToken: String): AuthResponse

    suspend fun refresh(refreshToken: String): AuthResponse
}