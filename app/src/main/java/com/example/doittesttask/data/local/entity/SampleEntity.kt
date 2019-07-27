package com.example.doittesttask.data.local.entity

import androidx.room.*

@Entity(tableName = "sample")
data class SampleEntity(
    @ColumnInfo(name = "sample_id")
    @PrimaryKey
    val sampleId: Int
)