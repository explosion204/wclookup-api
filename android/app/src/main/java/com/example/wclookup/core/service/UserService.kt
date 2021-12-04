package com.example.wclookup.core.service

import com.example.wclookup.core.model.User

interface UserService {
    suspend fun getById(accessToken: String, id: Long): User

    suspend fun update(accessToken: String, id: Long, user: User): User
}