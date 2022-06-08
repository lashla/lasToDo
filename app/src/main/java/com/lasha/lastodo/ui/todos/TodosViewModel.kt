package com.lasha.lastodo.ui.todos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel() {
    val todosData = MutableLiveData<List<Todos>>()
    init {
        val todosData = MutableLiveData<List<Todos>>()
        getAllData()
    }

    fun getAllData(){
        viewModelScope.launch(Dispatchers.Main) {
            roomRepository.getAllTodos().let {
                if (it.isNotEmpty()){
                    todosData.value = it
                    Log.i("DatabaseData", it.toString())
                    Log.i("GetData", "Database data fetched")
                }
            }
        }
    }
    fun getSortedByDeadline(){
        viewModelScope.launch(Dispatchers.Main) {
            roomRepository.getSortedDeadline().let {
                if (it.isNotEmpty()){
                    todosData.value = it
                }
            }
        }
    }
    fun getSortedByDate(){
        viewModelScope.launch(Dispatchers.Main) {
            roomRepository.getSortedTodos().let {
                if (it.isNotEmpty()){
                    todosData.value = it
                }
            }
        }
    }
}