package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant.CREATION_TIME
import com.example.wclookup.core.constant.NameConstant.ID
import com.example.wclookup.core.constant.NameConstant.RATING
import com.example.wclookup.core.constant.NameConstant.TEXT
import com.example.wclookup.core.constant.NameConstant.TOILET_ID
import com.example.wclookup.core.constant.NameConstant.USER_ID
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Review(
    @SerializedName(ID) var id: Long,
    @SerializedName(USER_ID) @Expose val userId: Long,
    @SerializedName(TOILET_ID) @Expose val toiletId: Long,
    @SerializedName(RATING) @Expose val rating: Int,
    @SerializedName(TEXT) @Expose val text: String,
)