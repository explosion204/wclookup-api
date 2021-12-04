package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Ticket
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.service.TicketService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class TicketServiceImpl : TicketService {
    override suspend fun create(accessToken: String, ticket: Ticket): Ticket {
        val apiResponse: ApiResponse<Ticket> = NetworkService.instance
            .getTicketApi()
            .create(accessToken, ticket)

        if (apiResponse.errorMessage.isNotEmpty()) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Ticket>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.data), typeToken)
    }

}