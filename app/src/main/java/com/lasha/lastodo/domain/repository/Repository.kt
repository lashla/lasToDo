package com.lasha.lastodo.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.data.model.Todos
import javax.inject.Singleton

@Singleton
interface Repository {
    suspend fun getAllTodos(): List<Todos>
    suspend fun getSortedTodos(): List<Todos>
    suspend fun getSortedDeadline(): List<Todos>
    suspend fun insertTodo(todos: Todos)
    suspend fun deleteCurrentTodo(todos: Todos)
    suspend fun updateCurrentTodo(todos: Todos)
    suspend fun signUpWIthEmailPassword(email: String, password: String) : FirebaseUser?
    suspend fun signInWIthEmailPassword(email: String, password: String) : FirebaseUser?
}