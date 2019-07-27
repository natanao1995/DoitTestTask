package com.example.doittesttask.feature.tasklist.recycler

import android.os.Parcelable
import com.example.doittesttask.data.remote.entity.Priority
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskItem(
    val id: Long,
    val title: String,
    val dueBy: Long,
    val priority: Priority
) : Parcelable