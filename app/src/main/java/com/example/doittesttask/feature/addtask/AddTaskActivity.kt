package com.example.doittesttask.feature.addtask

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.architecture.exception.NoInternetException
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.feature.addtask.AddTaskViewModel.InputError.*
import kotlinx.android.synthetic.main.activity_add_task.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class AddTaskActivity : BaseActivity() {

    private val viewModel by viewModel<AddTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        setupUi()
        setupObserve()
    }

    private fun setupUi() {
        buttonSave.setOnClickListener {
            viewModel.saveTask(
                editTextTitle.text.toString(),
                when (radioGroupPriority.checkedRadioButtonId) {
                    R.id.radioLow -> Priority.LOW
                    R.id.radioMedium -> Priority.NORMAL
                    R.id.radioHigh -> Priority.HIGH
                    else -> null
                }
            )
        }

        editTextDateTime.setOnClickListener {
            val date = viewModel.dateTimeLiveData.value ?: Calendar.getInstance()
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateDate(year, month, dayOfMonth)

                    TimePickerDialog(
                        this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            viewModel.updateTime(hourOfDay, minute)
                        },
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        editTextDateTime.apply {
            inputType = InputType.TYPE_NULL
            setTextIsSelectable(false)
            setOnKeyListener(null)
        }
    }

    private fun setupObserve() {
        editTextDateTime.setText(viewModel.dateTimeLiveData.value.toString())
        viewModel.dateTimeLiveData.observe(this, Observer { dateTime ->
            updateDateTime(dateTime)
        })
        viewModel.inputErrorsLiveData.observe(this, Observer { errors ->
            updateInputErrors(errors)
        })
        viewModel.saveTaskResultLiveData.observe(this, Observer { result ->
            reactToSaveTask(result)
        })
    }

    private fun reactToSaveTask(result: Result<Unit>?) {
        result ?: return

        when (result) {
            is ResultSuccess -> {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            is ResultError -> {
                when (result.exception) {
                    is NoInternetException -> showError(getString(R.string.errorNoInternetConnection))
                    else -> showError(getString(R.string.errorUnexpected))
                }
            }
        }
    }

    private fun updateInputErrors(errors: MutableList<AddTaskViewModel.InputError>?) {
        errors ?: return

        if (errors.isNotEmpty()) showError(getString(R.string.addTaskCheckFields))
        editTextTitle.error = if (errors.contains(EMPTY_TITLE)) getString(R.string.addTaskErrorEmptyTitle) else null
        editTextDateTime.error = if (errors.contains(EMPTY_DATE)) getString(R.string.addTaskErrorEmptyDate) else null
        textErrorEmptyPriority.visibility = if (errors.contains(EMPTY_PRIORITY)) View.VISIBLE else View.GONE
    }

    private fun updateDateTime(dateTime: Calendar?) {
        dateTime ?: return

        editTextDateTime.error = null
        editTextDateTime.setText(
            DateUtils.formatDateTime(
                this,
                dateTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
            )
        )
    }
}
