package com.lasha.lastodo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.data.db.TodosDao
import com.lasha.lastodo.data.remote.RemoteService
import com.lasha.lastodo.domain.repository.Repository
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val todosDao: TodosDao, private val remoteService: RemoteService): Repository {
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

    override suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser? {
        return remoteService.signUpWIthEmailPassword(email, password)
    }

    override suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        return remoteService.signInWIthEmailPassword(email, password)
    }

    override suspend fun saveTodoToFirestore(todos: Todos) {
        remoteService.saveTodoToFirestore(todos)
    }

    override suspend fun saveTodosToFirestore(todos: List<Todos>) {
        remoteService.saveTodosToFirestore(todos)
    }

    override suspend fun updateFirestore(id: Int, todo: Todos) {
        remoteService.updateFirestore(id, todo)
    }

    override suspend fun getFromFirestore(): List<Todos> {
        return remoteService.getFromFirestore()
    }

    override suspend fun uploadImage(path: Uri) {
        remoteService.uploadImage(path)
    }

    override suspend fun getImage(): String {
        return "DD"
    }
}