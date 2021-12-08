package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.Review
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.network.api.WclookupReviewApi
import com.example.wclookup.core.network.api.WclookupToiletApi
import com.example.wclookup.core.service.ReviewService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

class ReviewServiceImpl : ReviewService {
    private val MAX_PAGE_SIZE = 50

    override suspend fun getAll(
        accessToken: String, toiletId: Long
    ): List<Review> {
        val reviews: MutableList<Review> = LinkedList()
        val reviewApi: WclookupReviewApi = NetworkService.instance.getReviewApi()
        val gson = GsonBuilder().create()
        var pageNumber = 1

        var apiResponse = reviewApi
            .getAll(accessToken, toiletId, pageNumber, MAX_PAGE_SIZE)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        var typeToken = object : TypeToken<List<Review>>() {}.type
        reviews.addAll(gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken))

        while (reviews.size < apiResponse.body()?.totalEntities!!.toInt()) {
            pageNumber++
            apiResponse = reviewApi
                .getAll(accessToken, toiletId, pageNumber, MAX_PAGE_SIZE)

            if (!apiResponse.isSuccessful) {
                throw AccessTokenException()
            }

            typeToken = object : TypeToken<List<Review>>() {}.type
            reviews.addAll(gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken))
        }
        return reviews
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
        return gson.fromJson(gson.toJson(apiResponse.body()), typeToken)
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
        return gson.fromJson(gson.toJson(apiResponse.body()), typeToken)
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
        return gson.fromJson(gson.toJson(apiResponse.body()), typeToken)
    }

    override suspend fun delete(accessToken: String, id: Long) {
        NetworkService.instance
            .getReviewApi()
            .delete(accessToken, id)
    }
}