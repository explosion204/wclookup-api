package com.example.wclookup.core.service.impl

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.exception.RefreshTokenException
import com.example.wclookup.core.model.AuthResponse
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.validation.AccessTokenValidator
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

class AuthServiceImpl : AuthService {
    override suspend fun authenticate(idToken: String): AuthResponse {
        val authResponse = NetworkService.instance
            .getAuthApi()
            .authenticate(idToken)

        if (!authResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        return gson.fromJson(gson.toJson(authResponse.body()), AuthResponse::class.java)
    }

    override suspend fun refresh(refreshToken: String): AuthResponse {
        val authResponse = NetworkService.instance
            .getAuthApi()
            .refresh(refreshToken)

        if (!authResponse.isSuccessful) {
            throw RefreshTokenException()
        }

        val gson = GsonBuilder().create()
        return gson.fromJson(gson.toJson(authResponse.body()), AuthResponse::class.java)
    }
}