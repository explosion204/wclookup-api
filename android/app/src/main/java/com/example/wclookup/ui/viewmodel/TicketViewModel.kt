package com.example.wclookup.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.model.Ticket
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.service.AuthService
import com.example.wclookup.core.service.TicketService
import com.example.wclookup.core.validation.AccessTokenValidator
import kotlinx.coroutines.launch
import javax.inject.Inject

class TicketViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val ticketService: TicketService,
    private val authService: AuthService,
): ViewModel() {
    lateinit var ticket: Ticket

    fun createTicket() {
        viewModelScope.launch {
            var accessToken = preferences.getString(NameConstant.ACCESS_TOKEN, "")!!
            accessToken = validateAccessToken(accessToken)

            ticketService.create(accessToken, ticket)
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