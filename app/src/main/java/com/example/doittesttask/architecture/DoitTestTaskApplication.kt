package com.example.doittesttask.architecture

import android.app.Application
import com.example.doittesttask.architecture.di.AuthModule.authModule
import com.example.doittesttask.architecture.di.DatabaseModule.databaseModule
import com.example.doittesttask.architecture.di.InteractorModule.interactorModule
import com.example.doittesttask.architecture.di.NetworkModule.networkModule
import com.example.doittesttask.architecture.di.ViewModelModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DoitTestTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DoitTestTaskApplication)

            modules(
                authModule,
                viewModelModule,
                interactorModule,
                networkModule,
                databaseModule)
        }
    }
}