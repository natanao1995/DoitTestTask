package com.example.doittesttask.architecture.di

import android.content.Context
import androidx.room.Room
import com.example.doittesttask.Constants.DATABASE_NAME
import com.example.doittesttask.data.local.DoitDatabase
import org.koin.dsl.module

object DatabaseModule {
    val databaseModule = module {
        single { createAppDatabase(get()) }
//        single { createSavedMovieDao(get()) }
    }

    private fun createAppDatabase(applicationContext: Context): DoitDatabase {
        return Room.databaseBuilder(
            applicationContext,
            DoitDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

//    private fun createSavedMovieDao(appDatabase: DoitDatabase): SavedMovieDao {
//        return appDatabase.savedMovieDao()
//    }
}