package com.lasha.lastodo.data.db

import androidx.room.*
import com.lasha.lastodo.data.model.Todo

@Dao
interface TodosDao {

    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<Todo>

    @Insert(entity = Todo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todos: Todo)

    @Insert(entity = Todo::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocalTodos(todos: List<Todo>)

    @Query("SELECT * FROM todos ORDER BY date ASC")
    suspend fun getSortedByDate(): List<Todo>

    @Query("SELECT * FROM todos ORDER BY deadline_date ASC")
    suspend fun getSortedByDeadline(): List<Todo>

    @Delete(entity = Todo::class)
    suspend fun deleteLocalTodo(todos: Todo)

    @Query("DELETE FROM todos")
    suspend fun clearData()

    @Update(entity = Todo::class)
    suspend fun updateLocalTodo(todos: Todo)
}