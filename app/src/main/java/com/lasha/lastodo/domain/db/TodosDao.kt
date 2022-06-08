package com.lasha.lastodo.domain.db

import androidx.room.*
import com.lasha.lastodo.data.model.Todos

@Dao
interface TodosDao {
    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<Todos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun intert(todos: Todos)

    @Query("SELECT * FROM todos ORDER BY date ASC")
    suspend fun getSortedByDate(): List<Todos>

    @Query("SELECT * FROM todos ORDER BY deadline_date ASC")
    suspend fun getSortedByDeadline(): List<Todos>

    @Delete(entity = Todos::class)
    suspend fun deleteCurrentTodo(todos: Todos)

    @Update(entity = Todos::class)
    suspend fun updateCurrentTodo(todos: Todos)

}