package com.example.doittesttask.data.mapper

import com.example.doittesttask.data.remote.entity.TaskResponseBody
import com.example.doittesttask.feature.tasklist.recycler.TaskItem
import com.example.doittesttask.util.fromUnixToMillis

fun TaskResponseBody.toUiEntity() = TaskItem(id, title, dueBy.fromUnixToMillis(), priority)