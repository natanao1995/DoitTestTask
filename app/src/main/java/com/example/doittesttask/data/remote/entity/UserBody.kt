package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

data class UserBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)