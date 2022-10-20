package com.lasha.lastodo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.data.db.TodosDao
import com.lasha.lastodo.data.remote.RemoteService
import com.lasha.lastodo.domain.repository.Repository
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

    override suspend fun insertTodos(todos: List<Todos>) {
        return todosDao.insertTodos(todos)
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
        remoteService.saveTodoToFirebase(todos)
    }

    override suspend fun saveTodosToFirestore(todos: List<Todos>) {
        remoteService.saveTodosToFirebase(todos)
    }

    override suspend fun updateFirestore(id: Int, todo: Todos) {
        remoteService.updateFirebase(id, todo)
    }

    override suspend fun getFromFirestore(): List<Todos> {
        return remoteService.getFromFirebase()
    }

    override suspend fun uploadImage(path: Uri) {
        remoteService.uploadImage(path)
    }

    override suspend fun getImage(): String {
        return "DD"
    }

    override suspend fun checkLoginState(): Boolean {
        return remoteService.checkLoginState()
    }

    override suspend fun logout() {
        remoteService.logout()
    }

    override suspend fun downloadFile(fileName: String) {
        return remoteService.downloadFile(fileName)
    }

    override suspend fun clearData() {
        return todosDao.clearData()
    }

    override suspend fun getUser(): FirebaseUser? {
        return remoteService.getUser()
    }
}