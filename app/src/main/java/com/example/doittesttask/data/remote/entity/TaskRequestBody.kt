package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

data class TaskRequestBody(
    @SerializedName("title")
    val title: String,
    @SerializedName("dueBy")
    val dueBy: String,
    @SerializedName("priority")
    val priority: String
)