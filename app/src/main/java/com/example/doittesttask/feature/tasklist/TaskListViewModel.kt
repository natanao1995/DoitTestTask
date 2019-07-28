package com.example.doittesttask.feature.tasklist

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.doittesttask.architecture.base.BaseViewModel
import com.example.doittesttask.data.remote.entity.SortQuery.SortOrder
import com.example.doittesttask.data.remote.entity.SortQuery.SortType
import com.example.doittesttask.data.remote.entity.TasksListBody.Meta.Companion.PAGE_SIZE
import com.example.doittesttask.feature.auth.AuthManager
import com.example.doittesttask.feature.tasklist.recycler.TaskDataSourceFactory
import kotlinx.coroutines.launch

class TaskListViewModel(
    interactor: TaskListInteractor,
    private val authManager: AuthManager
) : BaseViewModel() {
    var sortType = SortType.BY_EXPIRATION_TIME
        private set
    var sortOrder = SortOrder.ASCENDING
        private set

    private val taskDataSourceFactory = TaskDataSourceFactory(interactor, sortType, sortOrder)
    val grabHistoryItemPagedList = LivePagedListBuilder(
        taskDataSourceFactory,
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE).build()
    ).build()

    fun refreshTasks() {
        taskDataSourceFactory.sortType = sortType
        taskDataSourceFactory.sortOrder = sortOrder
        taskDataSourceFactory.taskDataSourceLiveData.value?.invalidate()
    }

    fun updateSortType(type: SortType) {
        sortType = type
        refreshTasks()
    }

    fun updateSortOrder(order: SortOrder) {
        sortOrder = order
        refreshTasks()
    }

    fun logout() {
        launch {
            authManager.logout(true)
        }
    }
}