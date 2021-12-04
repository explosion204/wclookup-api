package com.example.wclookup.core.network

import com.example.wclookup.core.network.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService private constructor() {
    private val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getToiletApi(): WclookupToiletApi {
        return retrofitInstance.create(WclookupToiletApi::class.java)
    }

    fun getTicketApi(): WclookupTicketApi {
        return retrofitInstance.create(WclookupTicketApi::class.java)
    }

    fun getUserApi(): WclookupUserApi {
        return retrofitInstance.create(WclookupUserApi::class.java)
    }

    fun getReviewApi(): WclookupReviewApi {
        return retrofitInstance.create(WclookupReviewApi::class.java)
    }

    fun getAuthApi(): WclookupAuthApi {
        return retrofitInstance.create(WclookupAuthApi::class.java)
    }

    companion object {
        // TODO: fetch from properties
        private const val BASE_URL = "https://wclookup.online"
        val instance = NetworkService()
    }
}