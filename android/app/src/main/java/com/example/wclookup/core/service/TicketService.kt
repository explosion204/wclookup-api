package com.example.wclookup.core.service

import com.example.wclookup.core.model.Ticket

interface TicketService {
    suspend fun create(accessToken: String, ticket: Ticket): Ticket
}