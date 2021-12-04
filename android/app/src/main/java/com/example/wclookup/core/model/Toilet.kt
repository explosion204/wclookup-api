package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant.ADDRESS
import com.example.wclookup.core.constant.NameConstant.CONFIRMED
import com.example.wclookup.core.constant.NameConstant.ID
import com.example.wclookup.core.constant.NameConstant.LATITUDE
import com.example.wclookup.core.constant.NameConstant.LONGITUDE
import com.example.wclookup.core.constant.NameConstant.RATING
import com.example.wclookup.core.constant.NameConstant.SCHEDULE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Toilet(
    @SerializedName(ID) val id: Long,
    @SerializedName(ADDRESS) @Expose val address: String,
    @SerializedName(SCHEDULE) @Expose val schedule: String,
    @SerializedName(LATITUDE) @Expose val latitude: Double,
    @SerializedName(LONGITUDE) @Expose val longitude: Double,
    @SerializedName(RATING) @Expose val rating: Double,
    @SerializedName(CONFIRMED) @Expose val confirmed: Boolean
)