package com.example.doittesttask.architecture.di

import com.example.doittesttask.data.User
import com.example.doittesttask.feature.auth.AuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AuthModule {
    val authModule = module {
        single { getUser() }
        single { AuthManager(androidContext(), get()) }
    }

    private fun getUser(): User {
        return User()
    }
}