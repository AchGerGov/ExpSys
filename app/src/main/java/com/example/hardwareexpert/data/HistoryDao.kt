package com.example.hardwareexpert.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY time DESC")
    suspend fun getAll(): List<HistoryEntity>

    @Insert
    suspend fun insert(item: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clear()
}
