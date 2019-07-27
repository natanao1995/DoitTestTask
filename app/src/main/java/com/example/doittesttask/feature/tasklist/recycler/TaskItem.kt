package com.example.doittesttask.feature.tasklist.recycler

import com.example.doittesttask.data.remote.entity.Priority

data class TaskItem(
    val id: Int,
    val title: String,
    val dueBy: Long,
    val priority: Priority
)