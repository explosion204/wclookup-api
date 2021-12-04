package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant.DATA
import com.example.wclookup.core.constant.NameConstant.ERROR_MESSAGE
import com.example.wclookup.core.constant.NameConstant.PAGE_NUMBER
import com.example.wclookup.core.constant.NameConstant.PAGE_SIZE
import com.example.wclookup.core.constant.NameConstant.TOTAL_ENTITIES
import com.example.wclookup.core.constant.NameConstant.TOTAL_PAGES
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName(DATA) val data: T,
    @SerializedName(PAGE_NUMBER) val pageNumber: Int,
    @SerializedName(PAGE_SIZE) val pageSize: Int,
    @SerializedName(TOTAL_PAGES) val totalPages: String,
    @SerializedName(TOTAL_ENTITIES) val totalEntities: String
)