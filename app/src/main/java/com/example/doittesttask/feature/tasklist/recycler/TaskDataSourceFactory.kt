package com.example.doittesttask.feature.tasklist.recycler

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.doittesttask.data.remote.entity.SortQuery.SortOrder
import com.example.doittesttask.data.remote.entity.SortQuery.SortType
import com.example.doittesttask.feature.tasklist.TaskListInteractor

class TaskDataSourceFactory(
    private val interactor: TaskListInteractor,
    var sortType: SortType,
    var sortOrder: SortOrder
) : DataSource.Factory<Int, TaskItem>() {

    val taskDataSourceLiveData = MutableLiveData<TaskDataSource>()

    override fun create(): DataSource<Int, TaskItem> {
        val dataSource = TaskDataSource(interactor, sortType, sortOrder)
        taskDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}
