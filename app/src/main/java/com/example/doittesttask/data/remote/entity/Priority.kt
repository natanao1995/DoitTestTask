package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

enum class Priority {
    @SerializedName("Low")
    LOW,
    @SerializedName("Normal")
    NORMAL,
    @SerializedName("High")
    HIGH
}