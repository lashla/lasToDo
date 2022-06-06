package com.lasha.lastodo.ui.show_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowTodoViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel() {

    fun deleteTodo(todos: Todos){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.deleteCurrentTodo(todos)
        }
    }

    fun updateTodo(todos: Todos){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.updateCurrentTodo(todos)
        }
    }
}