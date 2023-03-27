package com.lasha.lastodo.ui.profile

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
class ProfileViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var user = MutableLiveData<FirebaseUser>()

    init {
        viewModelScope.launch(Dispatchers.IO){
            user.postValue(repository.getUser())
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearLocalData()
            repository.logout()
        }
    }
}