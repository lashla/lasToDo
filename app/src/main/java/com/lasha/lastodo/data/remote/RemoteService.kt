package com.lasha.lastodo.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.lasha.lastodo.data.model.Todo
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class RemoteService(
    private val firebaseAuth: FirebaseAuth,
    private val FireStore: FirebaseFirestore) {

    suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser
    }

    suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun checkLoginState(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun saveTodoToFirebase(todos: Todo) {
        FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").add(todos).await()
    }

    suspend fun saveTodosToFirebase(todos: List<Todo>) {
        for (element in todos) {
            FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                .collection("todos").add(element).await()
        }
    }

    suspend fun deleteTodoFromRemote(todo: Todo) {
        val querySnapshot =
            FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                .collection("todos").whereEqualTo("id", todo.id).get().await()
        if (querySnapshot.documents.isNotEmpty()) {
            for (document in querySnapshot) {
                FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                    .collection("todos").document(document.id).delete().await()
            }
        }
    }

    suspend fun updateFirebase(id: Int, todo: Todo) {
        val querySnapshot =
            FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                .collection("todos").whereEqualTo("id", id).get().await()
        if (querySnapshot.documents.isNotEmpty()) {
            for (document in querySnapshot) {
                FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                    .collection("todos").document(document.id).set(todo).await()
            }
        }
    }

    suspend fun getFromFirebase(): List<Todo> {
        val querySnapshot =
            FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                .collection("todos").get().await()
        val todoList = ArrayList<Todo>()
        for (document in querySnapshot) {
            todoList.add(document.toObject())
        }
        return todoList
    }
}