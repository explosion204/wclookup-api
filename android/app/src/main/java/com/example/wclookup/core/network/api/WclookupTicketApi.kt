package com.example.wclookup.core.network.api

import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.model.Ticket
import com.example.wclookup.core.model.Toilet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WclookupTicketApi {
    @POST("/api/tickets")
    suspend fun create(
        @Header("accessToken") accessToken: String,
        @Body ticket: Ticket
    ): Response<ApiResponse<Ticket>>
}