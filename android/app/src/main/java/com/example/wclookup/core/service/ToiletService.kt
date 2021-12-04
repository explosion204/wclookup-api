package com.example.wclookup.core.service

import com.example.wclookup.core.model.Toilet

interface ToiletService {
    suspend fun getAll(
        accessToken: String,
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Toilet>

    suspend fun getPage(
        accessToken: String,
        page: Int,
        size: Int,
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Toilet>

    suspend fun getById(accessToken: String, id: Long): Toilet

    suspend fun create(accessToken: String, toilet: Toilet): Toilet
}