package com.example.hardwareexpert.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val cause: String,
    val advice: String,
    val source: String,
    val time: Long = System.currentTimeMillis()
)
