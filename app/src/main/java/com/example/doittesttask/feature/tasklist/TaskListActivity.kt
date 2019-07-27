package com.example.doittesttask.feature.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultLoading
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.feature.addtask.AddTaskActivity
import kotlinx.android.synthetic.main.activity_tasks_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class TaskListActivity : BaseActivity() {

    private val viewModel by viewModel<TaskListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_list)

        setupUi()
        setupObserve()
    }

    private fun setupUi() {
        buttonGet.setOnClickListener {
            viewModel.getTasks()
        }

        buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    private fun setupObserve() {
        viewModel.tasksListLiveData.observe(this, Observer { tasksList ->
            when (tasksList) {
                is ResultSuccess -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, tasksList.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is ResultError -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, tasksList.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ResultLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}
