package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

data class TokenBody(
    @SerializedName("token")
    val token: String
)