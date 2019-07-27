package com.example.doittesttask.feature.tasklist

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.SortQuery
import com.example.doittesttask.data.remote.entity.TasksListBody

class TaskListInteractor(
    private val doitService: DoitService
) : BaseInteractor() {
    suspend fun getTasks(page: Int): Result<TasksListBody> {
        return processRequest {
            doitService.getTasks(page, SortQuery.TITLE_DESC)
        }
    }
}