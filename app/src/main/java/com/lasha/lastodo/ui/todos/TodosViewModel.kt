package com.lasha.lastodo.ui.todos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(private val roomRepository: Repository): ViewModel() {
    val todosData = MutableLiveData<List<Todos>>()
    val ioDispatcher = Dispatchers.IO

    fun getAllData(isInternetConnected: Boolean){
        viewModelScope.launch(ioDispatcher) {
            roomRepository.getAllTodos().let {
                if (it.isNotEmpty()){
                    if (isInternetConnected){
                        val todos = it.toMutableList()
                        val todosFromFirebaseStorage = roomRepository.getFromFirestore()
                        if (todosFromFirebaseStorage.size < it.size){
                            roomRepository.saveTodosToFirestore(it)
                        }
                        for (fireStoreItem in todosFromFirebaseStorage){
                            for (localItem in it){
                                if (checkIfLocalItemsUpToDate(fireStoreItem, localItem)){
                                    roomRepository.updateCurrentTodo(Todos(localItem.id, fireStoreItem.subject, fireStoreItem.contents, fireStoreItem.date, fireStoreItem.photoPath, fireStoreItem.deadlineDate))
                                    withContext(Dispatchers.Main){
                                        todos[localItem.id] =  Todos(localItem.id, fireStoreItem.subject, fireStoreItem.contents, fireStoreItem.date, fireStoreItem.photoPath, fireStoreItem.deadlineDate)
                                    }
                                }
                                if (checkIfRemoteItemsUpToDate(fireStoreItem, localItem)){
                                    roomRepository.updateFirestore(fireStoreItem.id, Todos(fireStoreItem.id, localItem.subject, localItem.contents, localItem.date, localItem.photoPath, localItem.deadlineDate))
                                }
                            }
                        }
                        todosData.postValue(todos)
                    } else {
                        todosData.postValue(it)
                    }
                    Log.i("DatabaseData", it.toString())
                    Log.i("GetData", "Database data fetched")
                }
            }
        }
    }
    fun getSortedByDeadline(){
        viewModelScope.launch(ioDispatcher) {
            roomRepository.getSortedDeadline().let {
                if (it.isNotEmpty()){
                    todosData.postValue(it)
                }
            }
        }
    }

    fun getSortedByDate(){
        viewModelScope.launch(ioDispatcher) {
            roomRepository.getSortedTodos().let {
                if (it.isNotEmpty()){
                    todosData.postValue(it)
                }
            }
        }
    }

    private fun checkIfLocalItemsUpToDate(remoteItem: Todos, localItem: Todos): Boolean{
        if (localItem.id == remoteItem.id && localItem.date < remoteItem.date){
            return true
        }
        return false
    }
    private fun checkIfRemoteItemsUpToDate(remoteItem: Todos, localItem: Todos): Boolean{
        if (localItem.id == remoteItem.id && localItem.date > remoteItem.date){
            return true
        }
        return false
    }
}