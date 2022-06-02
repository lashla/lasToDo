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

    override suspend fun insertTodo(todos: Todos) {
        return todosDao.intert(todos)
    }

    override suspend fun deleteCurrentTodo(id: Int?) {
        return todosDao.deleteCurrentTodo(id)
    }

    override suspend fun updateCurrentTodo(todos: Todos) {
        return todosDao.updateCurrentTodo(todos)
    }

}