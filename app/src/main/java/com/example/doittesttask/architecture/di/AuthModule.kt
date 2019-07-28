package com.example.doittesttask.architecture.di

import com.example.doittesttask.data.User
import org.koin.dsl.module

object AuthModule {
    val authModule = module {
        single { getUser() }
    }

    private fun getUser(): User {
        return User()
    }
}