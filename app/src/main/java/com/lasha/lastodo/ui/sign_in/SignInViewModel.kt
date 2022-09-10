package com.lasha.lastodo.ui.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val exception = MutableLiveData<String>()
    val currentUser = MutableLiveData<FirebaseUser?>()

    fun loginUser(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentUser.postValue(repository.signInWIthEmailPassword(email, password))
            } catch (e: Exception){
                exception.postValue(e.message)
            }
        }
    }
}