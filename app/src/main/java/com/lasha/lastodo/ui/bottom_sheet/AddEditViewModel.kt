package com.lasha.lastodo.ui.bottom_sheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val roomRepository: Repository): ViewModel() {
    private var id = 0

    fun insertHandler(subject: String, description: String, date: String, filePath: String?, deadlineDate: String?){
        viewModelScope.launch {
            insertDataIntoDatabase(subject, description, date, filePath, deadlineDate)
        }
    }
    private fun insertDataIntoDatabase(subject: String, description: String, date: String, filePath: String?, deadlineDate: String?){
        viewModelScope.launch(Dispatchers.IO){
            roomRepository.insertTodo(Todos(id, subject, description, date, filePath, deadlineDate))
            id++
            Log.i("Insert", "Inserted new todo")
            Log.i("Insert",
                Todos(id, subject, description, date, filePath, deadlineDate).toString()
            )
        }
    }
    fun updateTodo(todos: Todos){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.updateCurrentTodo(todos)
        }
    }
    fun insertDataToFireStore(subject: String, description: String, date: String, filePath: String?, deadlineDate: String?){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.saveTodoToFirestore(Todos(id, subject, description, date, filePath, deadlineDate))
        }
    }
    fun updateDataFireStore(id: Int, subject: String, description: String, date: String, filePath: String?, deadlineDate: String?){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.saveTodoToFirestore(Todos(id, subject, description, date, filePath, deadlineDate))
        }
    }
}