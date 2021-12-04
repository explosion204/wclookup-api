package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant.ID
import com.example.wclookup.core.constant.NameConstant.NICKNAME
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(ID) val id: Long,
    @SerializedName(NICKNAME) val nickname: String
)