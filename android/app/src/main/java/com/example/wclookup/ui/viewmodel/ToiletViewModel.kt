package com.example.wclookup.ui.viewmodel;

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.db.TinyDB
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.service.ReviewService
import com.example.wclookup.core.service.UserService
import com.example.wclookup.core.validation.AccessTokenValidator
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToiletViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val authService: AuthService,
    private val reviewService: ReviewService,
    private val userService: UserService
): ViewModel() {
    lateinit var toilet: Toilet
    val reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val nicknames: MutableMap<Long, String> = HashMap()

    fun findReviews(sleep: Boolean = false) {
        viewModelScope.launch {
            if (sleep) {
                Thread.sleep(500)
            }
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            val result = reviewService.getAll(accessToken, toilet.id)
            result.forEach {
                nicknames[it.id] = userService.getById(accessToken, it.userId).nickname
            }
            reviews.postValue(result)
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
