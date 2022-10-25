package com.lasha.lastodo.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.lasha.lastodo.data.model.Todos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Singleton

@Singleton
class RemoteService(private val firebaseAuth: FirebaseAuth, private val fireCloud: FirebaseStorage, private val FireStore: FirebaseFirestore) {

    suspend fun signUpWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        return firebaseAuth.currentUser
    }

    suspend fun signInWIthEmailPassword(email: String, password: String): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return firebaseAuth.currentUser
    }

    fun getUser():  FirebaseUser?{
        return firebaseAuth.currentUser
    }

    fun checkLoginState(): Boolean{
        return firebaseAuth.currentUser != null
    }

    suspend fun logout(){
        firebaseAuth.signOut()
    }

    suspend fun saveTodoToFirebase(todos: Todos) {
        FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").add(todos).await()
    }

    suspend fun saveTodosToFirebase(todos: List<Todos>) {
        for (element in todos){
            FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                .collection("todos").add(element).await()
        }
    }

    suspend fun updateFirebase(id: Int, todo: Todos) {
        val querySnapshot = FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").whereEqualTo("id", id).get().await()
        if (querySnapshot.documents.isNotEmpty()){
            for (document in querySnapshot){
                FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                    .collection("todos").document(document.id).set(todo).await()
            }
        }
    }

    suspend fun getFromFirebase(): List<Todos> {
        val querySnapshot = FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").get().await()
        val todoList = ArrayList<Todos>()
        for (document in querySnapshot){
            todoList.add(document.toObject())
        }
        return todoList
    }

    suspend fun uploadImage(path: Uri) {
        val name = path.lastPathSegment
        val ref = fireCloud.reference.child("Images/${firebaseAuth.currentUser!!.uid}/$name")
        val uploadTask = ref.putFile(path)
        uploadTask.await()
    }

    suspend fun downloadFile(fileName: String){
        val ref = fireCloud.reference.child("Images/${firebaseAuth.currentUser!!.uid}/$fileName")
        val file = withContext(Dispatchers.IO) {
            File.createTempFile("Images/$fileName", "png")
        }
        ref.getFile(file).addOnSuccessListener {
            Log.i("Got file", "true")
        }
    }

}