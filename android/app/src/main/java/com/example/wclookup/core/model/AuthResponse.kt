package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant
import com.example.wclookup.core.constant.NameConstant.ACCESS_TOKEN
import com.example.wclookup.core.constant.NameConstant.ERROR_MESSAGE
import com.example.wclookup.core.constant.NameConstant.REFRESH_TOKEN
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(ACCESS_TOKEN) val accessToken: String,
    @SerializedName(REFRESH_TOKEN) val refreshToken: String
)