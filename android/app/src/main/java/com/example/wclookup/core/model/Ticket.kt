package com.example.wclookup.core.model

import com.example.wclookup.core.constant.NameConstant.CREATION_TIME
import com.example.wclookup.core.constant.NameConstant.EMAIL
import com.example.wclookup.core.constant.NameConstant.ID
import com.example.wclookup.core.constant.NameConstant.RESOLVED
import com.example.wclookup.core.constant.NameConstant.SUBJECT
import com.example.wclookup.core.constant.NameConstant.TEXT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Ticket(
    @SerializedName(ID) val id: Long,
    @SerializedName(SUBJECT) @Expose val subject: String,
    @SerializedName(TEXT) @Expose val text: String,
    @SerializedName(EMAIL) @Expose val email: String,
    @SerializedName(CREATION_TIME) @Expose val creationTime: Instant,
    @SerializedName(RESOLVED) @Expose val resolved: Boolean,
)