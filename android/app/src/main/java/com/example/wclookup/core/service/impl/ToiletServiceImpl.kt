package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.ApiResponse
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.network.api.WclookupToiletApi
import com.example.wclookup.core.service.ToiletService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

class ToiletServiceImpl : ToiletService {
    val PAGE_SIZE = 100

    override suspend fun getAll(
        accessToken: String,
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Toilet> {
        val toilets: MutableList<Toilet> = LinkedList()
        val toiletApi: WclookupToiletApi = NetworkService.instance.getToiletApi()
        val gson = GsonBuilder().create()
        var pageNumber = 1

        var apiResponse = toiletApi
            .getAll(accessToken, pageNumber, PAGE_SIZE, latitude, longitude, radius)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        var typeToken = object : TypeToken<List<Toilet>>() {}.type
        toilets.add(gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken))

        while (toilets.size < apiResponse.body()?.totalEntities!!.toInt()) {
            pageNumber++
            apiResponse = toiletApi
                .getAll(accessToken, pageNumber, PAGE_SIZE, latitude, longitude, radius)

            if (!apiResponse.isSuccessful) {
                throw AccessTokenException()
            }

            typeToken = object : TypeToken<List<Toilet>>() {}.type
            toilets.add(gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken))
        }
        return toilets
    }

    override suspend fun getPage(
        accessToken: String,
        page: Int,
        size: Int,
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Toilet> {
        val apiResponse = NetworkService.instance
            .getToiletApi()
            .getAll(accessToken, page, size, latitude, longitude, radius)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<List<Toilet>>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun getById(accessToken: String, id: Long): Toilet {
        val apiResponse = NetworkService.instance
            .getToiletApi()
            .getById(accessToken, id)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Toilet>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }

    override suspend fun create(accessToken: String, toilet: Toilet): Toilet {
        val apiResponse = NetworkService.instance
            .getToiletApi()
            .create(accessToken, toilet)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<Toilet>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()?.data), typeToken)
    }
}