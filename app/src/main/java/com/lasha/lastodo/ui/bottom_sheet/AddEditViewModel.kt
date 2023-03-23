package com.lasha.lastodo.ui.bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todo
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
            roomRepository.insertLocalTodo(Todo(id, subject, description, date, filePath, deadlineDate))
            id++
        }
    }
    fun updateTodo(todos: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.updateLocalTodo(todos)
        }
    }
}