package com.lasha.lastodo.data.repository

import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.data.db.TodosDao
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.data.remote.RemoteService
import com.lasha.lastodo.data.utils.checkIfLocalItemsUpToDate
import com.lasha.lastodo.data.utils.checkIfRemoteItemsUpToDate
import com.lasha.lastodo.domain.repository.Repository
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val todosDao: TodosDao, private val remoteService: RemoteService) :
    Repository {

    override suspend fun getAllTodos(): List<Todo> {
        return todosDao.getAll()
    }

    override suspend fun getSortedTodos(): List<Todo> {
        return todosDao.getSortedByDate()
    }

    override suspend fun getSortedDeadline(): List<Todo> {
        return todosDao.getSortedByDeadline()
    }

    override suspend fun insertLocalTodo(todos: Todo) {
        return todosDao.insert(todos)
    }

    override suspend fun insertLocalTodos(todos: List<Todo>) {
        return todosDao.insertLocalTodos(todos)
    }

    override suspend fun deleteLocalTodo(todos: Todo) {
        return todosDao.deleteLocalTodo(todos)
    }

    override suspend fun updateLocalTodo(todos: Todo) {
        return todosDao.updateLocalTodo(todos)
    }

    override suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser? {
        return remoteService.signUpWIthEmailPassword(email, password)
    }

    override suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        return remoteService.signInWIthEmailPassword(email, password)
    }

    override suspend fun saveTodoToFirestore(todos: Todo) {
        remoteService.saveTodoToFirebase(todos)
    }

    override suspend fun saveTodosToFirestore(todos: List<Todo>) {
        remoteService.saveTodosToFirebase(todos)
    }

    override suspend fun updateFirestore(id: Int, todo: Todo) {
        remoteService.updateFirebase(id, todo)
    }

    override suspend fun deleteFromRemote(todo: Todo) {
        remoteService.deleteTodoFromRemote(todo)
    }

    override suspend fun getFromFirestore(): List<Todo> {
        return remoteService.getFromFirebase()
    }

    override suspend fun checkLoginState(): Boolean {
        return remoteService.checkLoginState()
    }

    override suspend fun logout() {
        remoteService.logout()
    }

    override suspend fun clearData() {
        return todosDao.clearData()
    }

    override suspend fun syncData(isInternetConnected: Boolean) {
        todosDao.getAll().let { localTodos ->
            if (localTodos.isNotEmpty()) {
                if (isInternetConnected) {
                    val todosFromFirebaseStorage = getFromFirestore()
                    if (todosFromFirebaseStorage.size < localTodos.size) {
                        saveTodosToFirestore(localTodos)
                    } else if (todosFromFirebaseStorage.size > localTodos.size) {
                        insertLocalTodos(todosFromFirebaseStorage)
                    }
                    for (fireStoreItem in todosFromFirebaseStorage) {
                        for (localItem in localTodos) {
                            if (checkIfLocalItemsUpToDate(fireStoreItem, localItem)) {
                                updateLocalTodo(
                                    Todo(
                                        localItem.id,
                                        fireStoreItem.subject,
                                        fireStoreItem.contents,
                                        fireStoreItem.date,
                                        fireStoreItem.photoPath,
                                        fireStoreItem.deadlineDate,
                                        fireStoreItem.photoLink
                                    )
                                )
                            }
                            if (checkIfRemoteItemsUpToDate(fireStoreItem, localItem)) {
                                updateFirestore(
                                    fireStoreItem.id,
                                    Todo(
                                        fireStoreItem.id,
                                        localItem.subject,
                                        localItem.contents,
                                        localItem.date,
                                        localItem.photoPath,
                                        localItem.deadlineDate,
                                        localItem.photoLink
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun getUser(): FirebaseUser? {
        return remoteService.getUser()
    }
}