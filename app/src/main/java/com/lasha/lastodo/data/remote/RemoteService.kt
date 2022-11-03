package com.lasha.lastodo.data.remote

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lasha.lastodo.data.model.Todos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
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

    suspend fun deleteTodoFromRemote(todo: Todos){
        val querySnapshot = FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").whereEqualTo("id", todo.id).get().await()
        if (querySnapshot.documents.isNotEmpty()){
            for (document in querySnapshot){
                FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                    .collection("todos").document(document.id).delete().await()
            }
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

    suspend fun uploadImage(path: Uri, id: Int) {
        val name = path.lastPathSegment
        val ref = fireCloud.reference.child("Images/${firebaseAuth.currentUser!!.uid}/$name")
        val querySnapshot = FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
            .collection("todos").whereEqualTo("id", id).get().await()
        ref.putFile(path).addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener { uri ->
                if (querySnapshot.documents.isNotEmpty()){
                    for (document in querySnapshot){
                        FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                            .collection("todos").document(document.id).get().addOnSuccessListener { todo ->
                                todo.toObject<Todos>()?.let { item ->
                                    item.photoLink = uri.toString()
                                    FireStore.collection("userData").document(firebaseAuth.currentUser!!.uid)
                                        .collection("todos").document(document.id).set(item).addOnSuccessListener {
                                            Log.i("INSERTEEED", "AAAAAAAAAA")
                                        }
                                }
                            }
                    }
                }
            }
        }
    }

    suspend fun downloadFile(fileName: String, resolver: ContentResolver): Uri?{
        val ref = fireCloud.reference.child("Images/${firebaseAuth.currentUser!!.uid}/$fileName")
        return createFileInAppStorage(fileName, ref, resolver)
    }

    private suspend fun createFileInAppStorage(fileName: String, ref: StorageReference, resolver: ContentResolver): Uri?{
        val path = "Pictures/LasTODO"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            ref.getFile(uri).await()
        }
        return uri
    }
}