package com.lasha.lastodo.domain.repository

import android.net.Uri
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
    suspend fun saveTodoToFirestore(todos: Todos)
    suspend fun saveTodosToFirestore(todos: List<Todos>)
    suspend fun getFromFirestore(): List<Todos>
    suspend fun updateFirestore(id: Int, todo: Todos)
    suspend fun uploadImage(path: Uri)
    suspend fun getImage(): String
    suspend fun checkLoginState(): Boolean
    suspend fun logout()
    fun getUser():  FirebaseUser?
}