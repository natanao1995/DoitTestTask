package com.example.doittesttask.architecture.di

import com.example.doittesttask.data.User
import org.koin.dsl.module

object UserModule {
    val userModule = module {
        single { getUser() }
    }

    private fun getUser(): User {
        return User()
    }
}