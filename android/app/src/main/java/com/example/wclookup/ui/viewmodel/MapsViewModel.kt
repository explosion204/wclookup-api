package com.example.wclookup.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.service.ToiletService
import com.example.wclookup.core.validation.AccessTokenValidator
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val authService: AuthService,
    private val toiletService: ToiletService
) : ViewModel() {

    val toiletsList: MutableLiveData<List<Toilet>> = MutableLiveData()

    fun searchInRadius(latitude: Double, longitude: Double, radius: Double) {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            toiletsList.postValue(
                toiletService.getAll(accessToken, latitude, longitude, radius))
        }
    }

    fun addToilet(toilet: Toilet) {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            toiletService.create(accessToken, toilet)
        }
    }

    private fun validateAccessToken(accessToken: String): String {
        var newAccessToken: String = accessToken
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