package com.example.doittesttask.feature.tasklist.recycler

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.data.mapper.toUiEntity
import com.example.doittesttask.data.remote.entity.SortQuery
import com.example.doittesttask.data.remote.entity.TasksListBody.Meta.Companion.FIRST_PAGE
import com.example.doittesttask.feature.tasklist.TaskListInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskDataSource(
    private val interactor: TaskListInteractor,
    private val sortType: SortQuery.SortType,
    private val sortOrder: SortQuery.SortOrder
) : PageKeyedDataSource<Int, TaskItem>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TaskItem>) {
        GlobalScope.launch {
            when (val response = interactor.getTasks(FIRST_PAGE, sortType, sortOrder)) {
                is ResultSuccess -> {
                    val key = if (!response.data.meta.isLast()) FIRST_PAGE + 1 else null

                    callback.onResult(response.data.tasks.map { it.toUiEntity() }, null, key)
                }
                is ResultError -> Log.wtf("ERROR", response.exception.toString())
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TaskItem>) {
        GlobalScope.launch {
            when (val response = interactor.getTasks(params.key, sortType, sortOrder)) {
                is ResultSuccess -> {
                    val key = if (!response.data.meta.isFirst()) params.key - 1 else null

                    callback.onResult(response.data.tasks.map { it.toUiEntity() }, key)
                }
                is ResultError -> Log.wtf("ERROR", response.exception.toString())
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TaskItem>) {
        GlobalScope.launch {
            when (val response = interactor.getTasks(params.key, sortType, sortOrder)) {
                is ResultSuccess -> {
                    val key = if (!response.data.meta.isLast()) params.key + 1 else null

                    callback.onResult(response.data.tasks.map { it.toUiEntity() }, key)
                }
                is ResultError -> Log.wtf("ERROR", response.exception.toString())
            }
        }
    }
}