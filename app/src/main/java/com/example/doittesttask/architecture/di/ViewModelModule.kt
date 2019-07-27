package com.example.doittesttask.architecture.di

import com.example.doittesttask.feature.addtask.AddTaskViewModel
import com.example.doittesttask.feature.signin.SigninViewModel
import com.example.doittesttask.feature.taskdetails.TaskDetailsViewModel
import com.example.doittesttask.feature.tasklist.TaskListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel { SigninViewModel(get(), get()) }
        viewModel { TaskListViewModel(get()) }
        viewModel { AddTaskViewModel(get()) }
        viewModel { TaskDetailsViewModel(get()) }
    }
}