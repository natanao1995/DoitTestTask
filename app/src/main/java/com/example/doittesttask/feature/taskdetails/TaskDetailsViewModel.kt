package com.example.doittesttask.feature.taskdetails

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.BaseViewModel
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.util.notifyObserver
import kotlinx.coroutines.launch
import java.util.*

class TaskDetailsViewModel(
    private val interactor: TaskDetailsInteractor
) : BaseViewModel() {

    val dateTimeLiveData = MutableLiveData(
        Calendar.getInstance().also {
            it.add(Calendar.DAY_OF_YEAR, 1)
            it.set(Calendar.HOUR_OF_DAY, 8)
            it.set(Calendar.MINUTE, 0)
        }
    )

    var taskWasUpdated = false

    val modeLiveData = MutableLiveData(DetailsMode.VIEW)

    val inputErrorsLiveData = MutableLiveData(mutableListOf<InputError>())
    val updateTaskResultLiveData = MutableLiveData<Result<Unit>>()
    val deleteTaskResultLiveData = MutableLiveData<Result<Unit>>()

    private var taskId = 0L

    fun changeMode(mode: DetailsMode) {
        modeLiveData.value = mode
    }

    fun setTime(timeInMillis: Long) {
        dateTimeLiveData.value?.timeInMillis = timeInMillis
        dateTimeLiveData.notifyObserver()
    }

    fun setTaskId(id: Long) {
        taskId = id
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        dateTimeLiveData.value?.set(year, month, day)
        dateTimeLiveData.notifyObserver()
    }

    fun updateTime(hours: Int, minutes: Int) {
        dateTimeLiveData.value?.set(Calendar.HOUR_OF_DAY, hours)
        dateTimeLiveData.value?.set(Calendar.MINUTE, minutes)
        dateTimeLiveData.notifyObserver()
    }

    fun updateTask(title: String, priority: Priority?) {
        launch {
            val errors = mutableListOf<InputError>()

            if (title.isBlank()) errors.add(InputError.EMPTY_TITLE)
            val dateTime = dateTimeLiveData.value
            dateTime ?: errors.add(InputError.EMPTY_DATE)
            priority ?: errors.add(InputError.EMPTY_PRIORITY)

            if (errors.isEmpty()) {
                val result = interactor.updateTask(taskId, title, dateTime!!.timeInMillis, priority!!)

                if (result is ResultSuccess) {
                    modeLiveData.value = DetailsMode.VIEW
                    taskWasUpdated = true
                }

                updateTaskResultLiveData.value = result
            } else {
                inputErrorsLiveData.value = errors
            }
        }
    }

    fun deleteTask() {
        launch {
            deleteTaskResultLiveData.value = interactor.deleteTask(taskId)
        }
    }

    enum class DetailsMode {
        VIEW,
        EDIT
    }

    enum class InputError {
        EMPTY_TITLE,
        EMPTY_DATE,
        EMPTY_PRIORITY
    }
}