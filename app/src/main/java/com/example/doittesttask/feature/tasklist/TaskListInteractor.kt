package com.example.doittesttask.feature.tasklist

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.TasksListBody

class TaskListInteractor(
    private val doitService: DoitService
) : BaseInteractor() {
    suspend fun getTasks(): Result<TasksListBody> {
        return processRequest {
            doitService.getTasks()
        }
    }
}