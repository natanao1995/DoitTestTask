package com.example.doittesttask.feature.addtask

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.BaseViewModel
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.util.notifyObserver
import kotlinx.coroutines.launch
import java.util.*

class AddTaskViewModel(
    private val interactor: AddTaskInteractor
) : BaseViewModel() {

    val dateTimeLiveData = MutableLiveData(
        Calendar.getInstance().also {
            it.add(Calendar.DAY_OF_YEAR, 1)
            it.set(Calendar.HOUR_OF_DAY, 8)
            it.set(Calendar.MINUTE, 0)
        }
    )

    val inputErrorsLiveData = MutableLiveData(mutableListOf<InputError>())
    val saveTaskResultLiveData = MutableLiveData<Result<Unit>>()

    fun updateDate(year: Int, month: Int, day: Int) {
        dateTimeLiveData.value?.set(year, month, day)
        dateTimeLiveData.notifyObserver()
    }

    fun updateTime(hours: Int, minutes: Int) {
        dateTimeLiveData.value?.set(Calendar.HOUR_OF_DAY, hours)
        dateTimeLiveData.value?.set(Calendar.MINUTE, minutes)
        dateTimeLiveData.notifyObserver()
    }

    fun saveTask(title: String, priority: Priority?) {
        launch {
            val errors = mutableListOf<InputError>()

            if (title.isBlank()) errors.add(InputError.EMPTY_TITLE)
            val dateTime = dateTimeLiveData.value
            dateTime ?: errors.add(InputError.EMPTY_DATE)
            priority ?: errors.add(InputError.EMPTY_PRIORITY)

            if (errors.isEmpty()) {
                saveTaskResultLiveData.value = interactor.saveTask(title, dateTime!!.timeInMillis, priority!!)
            } else {
                inputErrorsLiveData.value = errors
            }
        }
    }

    enum class InputError {
        EMPTY_TITLE,
        EMPTY_DATE,
        EMPTY_PRIORITY
    }
}