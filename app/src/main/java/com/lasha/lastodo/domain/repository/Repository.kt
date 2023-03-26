package com.lasha.lastodo.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.data.model.Todo
import javax.inject.Singleton

@Singleton
interface Repository {
    suspend fun getAllTodos(): List<Todo>
    suspend fun getSortedTodos(): List<Todo>
    suspend fun getSortedDeadline(): List<Todo>
    suspend fun insertLocalTodo(todos: Todo)
    suspend fun insertLocalTodos(todos: List<Todo>)
    suspend fun deleteLocalTodo(todos: Todo)
    suspend fun updateLocalTodo(todos: Todo)
    suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser?
    suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser?
    suspend fun saveTodoToFirestore(todos: Todo)
    suspend fun saveTodosToFirestore(todos: List<Todo>)
    suspend fun getFromFirestore(): List<Todo>
    suspend fun updateFirestore(id: Int, todo: Todo)
    suspend fun deleteFromRemote(todo: Todo)
    suspend fun checkLoginState(): Boolean
    suspend fun logout()
    suspend fun getUser(): FirebaseUser?
    suspend fun clearData()
    suspend fun syncData(isInternetConnected: Boolean)
}