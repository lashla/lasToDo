package com.lasha.lastodo.ui.sign_in

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
class SignInViewModel: ViewModel() {

    val exception = MutableLiveData<String>()
    val nav = MutableLiveData<Boolean>()
    private var auth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email.lowercase(), password).await()
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