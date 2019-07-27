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
        val currentPage: Int,
        @SerializedName("limit")
        val itemsPerPage: Int,
        @SerializedName("count")
        val totalItemsCount: Int
    ) {
        companion object {
            const val FIRST_PAGE = 1
            const val PAGE_SIZE =
                15 //approximately, hope it doesn't change from time to time. But even if so it should work
        }

        val pagesNumber = totalItemsCount / itemsPerPage + 1
        val isFirst = currentPage == FIRST_PAGE
        val isLast = currentPage == pagesNumber
    }
}