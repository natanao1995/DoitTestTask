package com.example.doittesttask.feature.tasklist

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.*
import com.example.doittesttask.data.User
import com.example.doittesttask.data.remote.entity.TasksListBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TaskListViewModel(
    private val interactor: TaskListInteractor
) : BaseViewModel() {

    val tasksListLiveData = MutableLiveData<Result<TasksListBody>>()

    fun getTasks() {
        launch {
            tasksListLiveData.value = ResultLoading()
            tasksListLiveData.value = interactor.getTasks()
        }
    }
}