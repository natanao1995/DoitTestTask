package com.example.doittesttask.data.remote.entity

import com.google.gson.annotations.SerializedName

enum class SortQuery {
    @SerializedName("title asc")
    TITLE_ASC,
    @SerializedName("title desc")
    TITLE_DESC,
    @SerializedName("dueBy asc")
    DUE_BY_ASC,
    @SerializedName("dueBy desc")
    DUE_BY_DESC,
    @SerializedName("id asc")
    CREATION_TIME_ASC,
    @SerializedName("id desc")
    CREATION_TIME_DESC
}