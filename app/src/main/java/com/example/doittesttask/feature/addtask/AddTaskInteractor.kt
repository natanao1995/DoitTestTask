package com.example.doittesttask.feature.addtask

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.architecture.base.mapTo
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.data.remote.entity.TaskRequestBody
import com.example.doittesttask.util.fromMillisToUnix

class AddTaskInteractor(
    private val doitService: DoitService
) : BaseInteractor() {
    suspend fun saveTask(title: String, dueBy: Long, priority: Priority): Result<Unit> {
        return processRequest {
            doitService.addTask(TaskRequestBody(title, dueBy.fromMillisToUnix(), priority))
        }.mapTo { Unit }
    }
}