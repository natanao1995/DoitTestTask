package com.example.doittesttask.feature.addtask

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.TasksListBody

class AddTaskInteractor(
    private val doitService: DoitService
) : BaseInteractor() {

}