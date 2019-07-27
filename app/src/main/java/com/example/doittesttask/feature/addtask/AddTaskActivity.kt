package com.example.doittesttask.feature.addtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_task.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.text.InputType
import java.util.*
import android.text.format.DateUtils

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
            dateTime ?: return@Observer

            editTextDateTime.setText(
                DateUtils.formatDateTime(
                    this,
                    dateTime.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
                )
            )
        })
    }
}
