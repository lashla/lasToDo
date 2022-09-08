package com.lasha.lastodo.ui.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl): ViewModel() {

    val exception = MutableLiveData<String>()
    val currentUser = MutableLiveData<FirebaseUser?>()

    fun registerUser(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentUser.postValue(repositoryImpl.signUpWIthEmailPassword(email, password))
            } catch (e: Exception){
                exception.postValue(e.message)
            }
        }
    }
}