package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

data class TasksListBody(
    @SerializedName("tasks")
    val tasks: List<TaskResponseBody>,
    @SerializedName("meta")
    val meta: Meta
) {
    data class Meta(
        @SerializedName("current")
        val current: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("count")
        val count: Int
    )
}