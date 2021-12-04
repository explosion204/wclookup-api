package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.service.ReviewService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class ReviewServiceImpl : ReviewService {
    override suspend fun getAll(
        accessToken: String, toiletId: Long, page: Int, size: Int
    ): List<Review> {
        val apiResponse = NetworkService.instance
            .getReviewApi()
            .getAll(accessToken, toiletId, page, size)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun getById(accessToken: String, id: Long): Review {
        val apiResponse = NetworkService.instance
            .getReviewApi()
            .getById(accessToken, id)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Review>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun create(accessToken: String, review: Review): Review {
        val apiResponse = NetworkService.instance
            .getReviewApi()
            .create(accessToken, review)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Review>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun update(accessToken: String, id: Long, review: Review): Review {
        val apiResponse = NetworkService.instance
            .getReviewApi()
            .update(accessToken, id, review)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Review>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun delete(accessToken: String, id: Long) {
        val apiResponse = NetworkService.instance
            .getReviewApi()
            .delete(accessToken, id)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }
    }
}