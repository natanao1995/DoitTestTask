package com.example.doittesttask.feature.taskdetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.architecture.exception.NoInternetException
import com.example.doittesttask.data.remote.entity.Priority
import com.example.doittesttask.feature.taskdetails.TaskDetailsViewModel.InputError.*
import com.example.doittesttask.feature.tasklist.TaskListActivity
import com.example.doittesttask.feature.tasklist.recycler.TaskItem
import kotlinx.android.synthetic.main.activity_task_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class TaskDetailsActivity : BaseActivity() {

    private val viewModel by viewModel<TaskDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val task = intent.getParcelableExtra<TaskItem>(TaskListActivity.EXTRA_KEY_TASK)

        setupTask(task)
        setupUi()
        setupObserve()
    }

    private fun setupTask(task: TaskItem?) {
        if (task == null) {
            val intent = Intent()
            setResult(TaskListActivity.RESULT_CODE_ERROR, intent)
            finish()
            return
        }

        viewModel.setTime(task.dueBy)
        viewModel.setTaskId(task.id)
        editTextTitle.setText(task.title)
        when (task.priority) {
            Priority.LOW -> radioLow.performClick()
            Priority.NORMAL -> radioMedium.performClick()
            Priority.HIGH -> radioHigh.performClick()
        }
    }

    override fun onBackPressed() {
        if (viewModel.taskWasUpdated) {
            val intent = Intent()
            setResult(TaskListActivity.RESULT_CODE_NEED_REFRESH, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupUi() {
        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuDelete -> viewModel.deleteTask()
                R.id.menuEdit -> viewModel.changeMode(TaskDetailsViewModel.DetailsMode.EDIT)
                R.id.menuSave -> viewModel.updateTask(
                    editTextTitle.text.toString(),
                    when (radioGroupPriority.checkedRadioButtonId) {
                        R.id.radioLow -> Priority.LOW
                        R.id.radioMedium -> Priority.NORMAL
                        R.id.radioHigh -> Priority.HIGH
                        else -> null
                    }
                )
            }
            return@setOnMenuItemClickListener true
        }

        editTextTitle.doAfterTextChanged {
            textErrorEmptyTitle.visibility = if (it.isNullOrBlank()) View.VISIBLE else View.GONE
        }
    }

    private val changeDateTimeOnClickListener = View.OnClickListener {
        val date = viewModel.dateTimeLiveData.value ?: Calendar.getInstance()
        DatePickerDialog(
            this@TaskDetailsActivity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.updateDate(year, month, dayOfMonth)

                TimePickerDialog(
                    this@TaskDetailsActivity,
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

    private fun setupObserve() {
        viewModel.dateTimeLiveData.observe(this, Observer { dateTime ->
            updateDateTime(dateTime)
        })
        textDateTime.text = viewModel.dateTimeLiveData.value.toString()
        viewModel.inputErrorsLiveData.observe(this, Observer { errors ->
            updateInputErrors(errors)
        })
        viewModel.updateTaskResultLiveData.observe(this, Observer { result ->
            reactToUpdateTask(result)
        })
        viewModel.deleteTaskResultLiveData.observe(this, Observer { result ->
            reactToDeleteTask(result)
        })
        viewModel.modeLiveData.observe(this, Observer { mode ->
            changeMode(mode)
        })
        changeMode(viewModel.modeLiveData.value)
    }

    private fun changeMode(mode: TaskDetailsViewModel.DetailsMode?) {
        mode ?: return

        when (mode) {
            TaskDetailsViewModel.DetailsMode.VIEW -> {
                radioLow.visibility = if (radioLow.isChecked) View.VISIBLE else View.GONE
                radioMedium.visibility = if (radioMedium.isChecked) View.VISIBLE else View.GONE
                radioHigh.visibility = if (radioHigh.isChecked) View.VISIBLE else View.GONE
                space1.visibility = View.GONE
                space2.visibility = View.GONE
                textDateTime.setOnClickListener(null)
                editTextTitle.isEnabled = false
                hideKeyboard()
                editTextTitle.clearFocus()
                toolbar.menu?.findItem(R.id.menuEdit)?.isVisible = true
                toolbar.menu?.findItem(R.id.menuSave)?.isVisible = false
            }
            TaskDetailsViewModel.DetailsMode.EDIT -> {
                radioLow.visibility = View.VISIBLE
                radioMedium.visibility = View.VISIBLE
                radioHigh.visibility = View.VISIBLE
                space1.visibility = View.VISIBLE
                space2.visibility = View.VISIBLE
                textDateTime.setOnClickListener(changeDateTimeOnClickListener)
                editTextTitle.isEnabled = true
                editTextTitle.requestFocus()
                editTextTitle.setSelection(editTextTitle.length())
                toolbar.menu?.findItem(R.id.menuEdit)?.isVisible = false
                toolbar.menu?.findItem(R.id.menuSave)?.isVisible = true
            }
        }
    }

    private fun reactToDeleteTask(result: Result<Unit>?) {
        result ?: return

        when (result) {
            is ResultSuccess -> {
                val intent = Intent()
                setResult(TaskListActivity.RESULT_CODE_DELETE_TASK, intent)
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

    private fun reactToUpdateTask(result: Result<Unit>?) {
        result ?: return

        when (result) {
            is ResultSuccess -> {
                showMessage(getString(R.string.editTaskChangesSaved))
            }
            is ResultError -> {
                when (result.exception) {
                    is NoInternetException -> showError(getString(R.string.errorNoInternetConnection))
                    else -> showError(getString(R.string.errorUnexpected))
                }
            }
        }
    }

    private fun updateInputErrors(errors: MutableList<TaskDetailsViewModel.InputError>?) {
        errors ?: return

        if (errors.isNotEmpty()) showError(getString(R.string.addTaskCheckFields))
        textErrorEmptyTitle.visibility = if (errors.contains(EMPTY_TITLE)) View.VISIBLE else View.GONE
        textErrorEmptyDateTime.visibility = if (errors.contains(EMPTY_DATE)) View.VISIBLE else View.GONE
        textErrorEmptyPriority.visibility = if (errors.contains(EMPTY_PRIORITY)) View.VISIBLE else View.GONE
    }

    private fun updateDateTime(dateTime: Calendar?) {
        dateTime ?: return

        textDateTime.error = null
        textDateTime.text = DateUtils.formatDateTime(
            this,
            dateTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_TIME
        )
    }
}
