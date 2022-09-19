package com.lasha.lastodo.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.db.TodosDao
import com.lasha.lastodo.domain.repository.Repository
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val todosDao: TodosDao, private val firebaseAuth: FirebaseAuth, private val fireCloud: FirebaseStorage, private val firestore: FirebaseFirestore): Repository {
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
        firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        return firebaseAuth.currentUser
    }

    override suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser
    }

    override suspend fun saveTodoToFirestore(todos: Todos) {
        firestore.collection("todos").add(todos).await()
    }

    override suspend fun saveTodosToFirestore(todos: List<Todos>) {
        for (element in todos){
            firestore.collection("todos").add(element).await()
        }
    }

    override suspend fun getFromFirestore(): List<Todos> {
        val querySnapshot = firestore.collection("todos").get().await()
        val todoList = ArrayList<Todos>()
        for (document in querySnapshot){
            todoList.add(document.toObject(Todos::class.java))
        }
        return todoList
    }

    override suspend fun uploadImage(path: Uri) {
        val ref = fireCloud.reference.child("Images/")
        val uploadTask = ref.putFile(path)
        uploadTask.await()
    }

    override suspend fun getImage(): String {
        return "Todo"
    }
}