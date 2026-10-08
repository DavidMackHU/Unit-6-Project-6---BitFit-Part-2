package com.example.unit5project5_bitfitpart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BitFitDao {
    @Query("SELECT * FROM bitfit_table ORDER BY id DESC")
    fun getAll(): Flow<List<BitFitEntity>>

    @Insert
    suspend fun insert(item: BitFitEntity)

    @Query("DELETE FROM bitfit_table")
    suspend fun deleteAll()
}