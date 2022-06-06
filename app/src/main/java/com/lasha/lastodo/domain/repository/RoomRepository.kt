package com.lasha.lastodo.domain.repository

import com.lasha.lastodo.data.model.Todos
import javax.inject.Singleton

@Singleton
interface RoomRepository {
    suspend fun getAllTodos(): List<Todos>
    suspend fun getSortedTodos(): List<Todos>
    suspend fun insertTodo(todos: Todos)
    suspend fun deleteCurrentTodo(todos: Todos)
    suspend fun updateCurrentTodo(todos: Todos)
}