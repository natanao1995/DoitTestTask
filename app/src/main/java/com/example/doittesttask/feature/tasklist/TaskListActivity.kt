package com.example.doittesttask.feature.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import com.example.doittesttask.feature.addtask.AddTaskActivity
import com.example.doittesttask.feature.taskdetails.TaskDetailsActivity
import com.example.doittesttask.feature.tasklist.recycler.TaskItem
import com.example.doittesttask.feature.tasklist.recycler.TaskRecyclerAdapter
import kotlinx.android.synthetic.main.activity_tasks_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class TaskListActivity : BaseActivity() {

    companion object {
        const val REQUEST_CODE_ADD_TASK = 1
        const val REQUEST_CODE_EDIT_TASK = 2
        const val RESULT_CODE_DELETE_TASK = 3
        const val RESULT_CODE_NEED_REFRESH = 4
        const val RESULT_CODE_ERROR = 5
        const val EXTRA_KEY_TASK = "task"
    }

    private val viewModel by viewModel<TaskListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_list)

        setupUi()
        setupRecycler()
        setupObserve()
    }

    private fun setupUi() {
        buttonAdd.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), REQUEST_CODE_ADD_TASK)
        }
    }

    private fun setupRecycler() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshTasks()
        }
        val adapter = object : TaskRecyclerAdapter() {
            override fun onItemClick(task: TaskItem) {
                super.onItemClick(task)
                val intent = Intent(this@TaskListActivity, TaskDetailsActivity::class.java)
                intent.putExtra(EXTRA_KEY_TASK, task)
                startActivityForResult(intent, REQUEST_CODE_EDIT_TASK)
                showMessage(task.toString())
            }
        }
        recyclerTasks.adapter = adapter
        viewModel.grabHistoryItemPagedList.observe(this, Observer pageObserver@{
            it ?: return@pageObserver
            adapter.submitList(it)
            swipeRefreshLayout.isRefreshing = false
        })
    }

    private fun setupObserve() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == Activity.RESULT_OK) {
            showMessage(getString(R.string.addTaskWasSaved))
            viewModel.refreshTasks()
        } else if (requestCode == REQUEST_CODE_EDIT_TASK) {
            when (resultCode) {
                RESULT_CODE_DELETE_TASK -> {
                    showMessage(getString(R.string.editTaskWasSaved))
                    viewModel.refreshTasks()
                }
                RESULT_CODE_NEED_REFRESH -> viewModel.refreshTasks()
                RESULT_CODE_ERROR -> showError(getString(R.string.errorUnexpected))
            }
        }
    }
}
