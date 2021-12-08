package com.example.wclookup.ui.viewmodel

import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.service.ReviewService
import com.example.wclookup.core.service.UserService
import com.example.wclookup.core.validation.AccessTokenValidator
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val reviewService: ReviewService,
    private val authService: AuthService,
    private val userService: UserService
): ViewModel() {
    lateinit var review: Review
    var userId: Long = 0

    fun createReview() {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            reviewService.create(accessToken, review)
        }
    }

    fun updateReview() {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            reviewService.update(accessToken, review.id, review)
        }
    }

    fun deleteReview(id: Long) {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            reviewService.delete(accessToken, id)
        }
    }

    fun getCurrentUserId() {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            userId = userService.getCurrent(accessToken).id
        }
    }

    private fun validateAccessToken(accessToken: String): String {
        var newAccessToken = accessToken
        viewModelScope.launch {
            if (!AccessTokenValidator.validateExpiryTime(accessToken)) {
                val refreshToken = preferences.getString(NameConstant.REFRESH_TOKEN, "")!!
                val authResponse = authService.refresh(refreshToken)
                newAccessToken = authResponse.accessToken

                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString(NameConstant.ACCESS_TOKEN, authResponse.accessToken)
                editor.putString(NameConstant.REFRESH_TOKEN, authResponse.refreshToken)
                editor.apply()
            }
        }
        return newAccessToken
    }
}