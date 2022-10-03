package com.lasha.lastodo.data.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.lasha.lastodo.data.model.Todos
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class RemoteService(private val firebaseAuth: FirebaseAuth, private val fireCloud: FirebaseStorage, private val firestore: FirebaseFirestore) {

    suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        return firebaseAuth.currentUser
    }

    suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser
    }

    suspend fun checkLoginState(): Boolean{
        return firebaseAuth.currentUser != null
    }

    suspend fun logout(){
        firebaseAuth.signOut()
    }

    suspend fun saveTodoToFirestore(todos: Todos) {
        firestore.collection("todos").add(todos).await()
    }

    suspend fun saveTodosToFirestore(todos: List<Todos>) {
        for (element in todos){
            firestore.collection("todos").add(element).await()
        }
    }

    suspend fun updateFirestore(id: Int, todo: Todos) {
        val querySnapshot = firestore.collection("todos").whereEqualTo("id", id).get().await()
        if (querySnapshot.documents.isNotEmpty()){
            for (document in querySnapshot){
                firestore.collection("todos").document(document.id).set(todo).await()
            }
        }
    }

    suspend fun getFromFirestore(): List<Todos> {
        val querySnapshot = firestore.collection("todos").get().await()
        val todoList = ArrayList<Todos>()
        for (document in querySnapshot){
            todoList.add(document.toObject<Todos>())
        }
        return todoList
    }

    suspend fun uploadImage(path: Uri) {
        val ref = fireCloud.reference.child("Images/")
        val uploadTask = ref.putFile(path)
        uploadTask.await()
    }


}