package com.lasha.lastodo.ui.todos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
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
    fun insertHandler(subject: String, description: String, date: String, filePath: String, deadlineDate: String){
        viewModelScope.launch {
            insertDataIntoDatabase(subject, description, date, filePath, deadlineDate)
        }
    }
    private fun insertDataIntoDatabase(subject: String, description: String, date: String, filePath: String, deadlineDate: String){
        viewModelScope.launch(Dispatchers.IO){
            roomRepository.insertTodo(Todos(subject, description, date, filePath, deadlineDate))
            Log.i("Insert", "Inserted new todo")
        }
    }

    private fun getAllData(){
        viewModelScope.launch(Dispatchers.Main) {
            roomRepository.getAllTodos().let {
                if (it.isNotEmpty()){
                    todosData.value = it
                    Log.i("GetData", "Database data fetched")
                }
            }
        }
    }
}