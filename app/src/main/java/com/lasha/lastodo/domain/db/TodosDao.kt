package com.lasha.lastodo.domain.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasha.lastodo.data.model.Todos

@Dao
interface TodosDao {
    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<Todos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun intert(todos: Todos)

    @Query("SELECT * FROM todos ORDER BY date ASC")
    suspend fun getSortedByDate(): List<Todos>

}