package com.example.wclookup.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.AuthResponse
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.validation.AccessTokenValidator
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val authService: AuthService
) : ViewModel() {

    fun authenticate(idToken: String) {
        viewModelScope.launch {
            val accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!

            if (!AccessTokenValidator.validateExpiryTime(accessToken)) {
                val refreshToken = preferences.getString(NameConstant.REFRESH_TOKEN, "")!!

                val authResponse: AuthResponse = try {
                    authService.refresh(refreshToken)
                } catch (e: AccessTokenException) {
                    authService.authenticate(idToken)
                }

                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString(NameConstant.ACCESS_TOKEN, authResponse.accessToken)
                editor.putString(NameConstant.REFRESH_TOKEN, authResponse.refreshToken)
                editor.apply()
            }
        }
    }
}