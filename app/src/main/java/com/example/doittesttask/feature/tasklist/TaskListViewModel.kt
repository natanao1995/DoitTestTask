package com.example.doittesttask.feature.tasklist

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.doittesttask.architecture.base.BaseViewModel
import com.example.doittesttask.data.remote.entity.TasksListBody.Meta.Companion.PAGE_SIZE
import com.example.doittesttask.feature.tasklist.recycler.TaskDataSourceFactory

class TaskListViewModel(
    interactor: TaskListInteractor
) : BaseViewModel() {

    private val taskDataSourceFactory = TaskDataSourceFactory(interactor)
    val grabHistoryItemPagedList = LivePagedListBuilder(
        taskDataSourceFactory,
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE).build()
    ).build()

    fun refreshTasks() {
        taskDataSourceFactory.taskDataSourceLiveData.value?.invalidate()
    }
}