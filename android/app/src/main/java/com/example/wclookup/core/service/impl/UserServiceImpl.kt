package com.example.wclookup.core.service.impl

import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.User
import com.example.wclookup.core.network.NetworkService
import com.example.wclookup.core.service.UserService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class UserServiceImpl : UserService {
    override suspend fun getById(accessToken: String, id: Long): User {
        val apiResponse = NetworkService.instance
            .getUserApi()
            .getById(accessToken, id)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<User>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()), typeToken)
    }

    override suspend fun update(accessToken: String, id: Long, user: User): User {
        val apiResponse = NetworkService.instance
            .getUserApi()
            .update(accessToken, id, user)

        if (!apiResponse.isSuccessful) {
            throw AccessTokenException()
        }

        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<User>() {}.type
        return gson.fromJson(gson.toJson(apiResponse.body()), typeToken)
    }
}