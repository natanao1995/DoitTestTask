package com.example.doittesttask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doittesttask.data.local.entity.SampleEntity

@Database(
    entities = [
        SampleEntity::class
    ],
    version = 1)
abstract class DoitDatabase : RoomDatabase() {
//    abstract fun savedMovieDao(): SavedMovieDao
}