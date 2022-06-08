package com.lasha.lastodo.data.repository

import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.db.TodosDao
import com.lasha.lastodo.domain.repository.RoomRepository
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl(private val todosDao: TodosDao): RoomRepository {
    override suspend fun getAllTodos(): List<Todos> {
        return todosDao.getAll()
    }

    override suspend fun getSortedTodos(): List<Todos> {
        return todosDao.getSortedByDate()
    }

    override suspend fun getSortedDeadline(): List<Todos> {
        return todosDao.getSortedByDeadline()
    }

    override suspend fun insertTodo(todos: Todos) {
        return todosDao.intert(todos)
    }

    override suspend fun deleteCurrentTodo(todos: Todos) {
        return todosDao.deleteCurrentTodo(todos)
    }

    override suspend fun updateCurrentTodo(todos: Todos) {
        return todosDao.updateCurrentTodo(todos)
    }

}