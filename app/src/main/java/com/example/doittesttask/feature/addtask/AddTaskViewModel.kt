package com.example.doittesttask.feature.addtask

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.*
import com.example.doittesttask.util.notifyObserver
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

    fun updateDate(year: Int, month: Int, day: Int) {
        dateTimeLiveData.value?.set(year, month, day)
        dateTimeLiveData.notifyObserver()
    }

    fun updateTime(hours: Int, minutes: Int) {
        dateTimeLiveData.value?.set(Calendar.HOUR_OF_DAY, hours)
        dateTimeLiveData.value?.set(Calendar.MINUTE, minutes)
        dateTimeLiveData.notifyObserver()
    }
}