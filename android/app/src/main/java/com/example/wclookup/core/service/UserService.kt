package com.example.wclookup.core.service

import com.example.wclookup.core.model.User
import retrofit2.Response
import retrofit2.http.Header

interface UserService {
    suspend fun getById(accessToken: String, id: Long): User

    suspend fun getCurrent(accessToken: String): User

    suspend fun update(accessToken: String, id: Long, user: User): User
}