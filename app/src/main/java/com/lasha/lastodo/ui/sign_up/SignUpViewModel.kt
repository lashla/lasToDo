package com.lasha.lastodo.ui.sign_up

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

@ViewModelScoped
class SignUpViewModel: ViewModel() {

    val exception = MutableLiveData<String>()
    val nav = MutableLiveData<Boolean>()
    private var auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("USer ", "Creation")
                auth.createUserWithEmailAndPassword(email.lowercase(), password).await()
                checkLoggedInState()
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    exception.postValue(e.message)
                }
            }
        }
    }
    private fun checkLoggedInState(){
        if (auth.currentUser != null){
            nav.postValue(true)
        } else nav.postValue(false)
    }
}