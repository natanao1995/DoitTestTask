package com.example.doittesttask.architecture.di

import com.example.doittesttask.feature.addtask.AddTaskInteractor
import com.example.doittesttask.feature.signin.SigninInteractor
import com.example.doittesttask.feature.taskdetails.TaskDetailsInteractor
import com.example.doittesttask.feature.tasklist.TaskListInteractor
import org.koin.dsl.module

object InteractorModule {
    val interactorModule = module {
        factory { SigninInteractor(get()) }
        factory { TaskListInteractor(get()) }
        factory { AddTaskInteractor(get()) }
        factory { TaskDetailsInteractor(get()) }
    }
}