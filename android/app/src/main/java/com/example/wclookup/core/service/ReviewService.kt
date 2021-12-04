package com.example.wclookup.core.service

import com.example.wclookup.core.model.Review

interface ReviewService {
    suspend fun getAll(accessToken: String, toiletId: Long, page: Int, size: Int): List<Review>

    suspend fun getById(accessToken: String, id: Long): Review

    suspend fun create(accessToken: String, review: Review): Review

    suspend fun update(accessToken: String, id: Long, review: Review): Review

    suspend fun delete(accessToken: String, id: Long)
}