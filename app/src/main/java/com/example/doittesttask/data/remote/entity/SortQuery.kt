package com.example.doittesttask.data.remote.entity

class SortQuery {
    enum class SortType(val value: String) {
        BY_TITLE("title"),
        BY_EXPIRATION_TIME("dueBy"),
        BY_CREATION_TIME("id")
    }

    enum class SortOrder(val value: String) {
        ASCENDING("asc"),
        DESCENDING("desc")
    }
}