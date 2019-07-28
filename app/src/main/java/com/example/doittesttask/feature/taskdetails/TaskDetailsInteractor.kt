package com.example.doittesttask.feature.taskdetails

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.data.remote.entity.TaskRequestBody
import com.example.doittesttask.util.fromMillisToUnix

class TaskDetailsInteractor(
    private val doitService: DoitService
) : BaseInteractor() {
    suspend fun updateTask(id: Long, title: String, dueBy: Long, priority: Priority): Result<Unit> {
        return processRequest {
            doitService.updateTask(id, TaskRequestBody(title, dueBy.fromMillisToUnix(), priority))
        }
    }

    suspend fun deleteTask(id: Long): Result<Unit> {
        return processRequest {
            doitService.deleteTask(id)
        }
    }
}