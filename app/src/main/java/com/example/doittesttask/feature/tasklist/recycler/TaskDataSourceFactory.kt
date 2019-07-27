package com.example.doittesttask.feature.tasklist.recycler

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.doittesttask.feature.tasklist.TaskListInteractor

class TaskDataSourceFactory(private val interactor: TaskListInteractor) : DataSource.Factory<Int, TaskItem>() {

    val taskDataSourceLiveData = MutableLiveData<TaskDataSource>()

    override fun create(): DataSource<Int, TaskItem> {
        val dataSource = TaskDataSource(interactor)
        taskDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}
