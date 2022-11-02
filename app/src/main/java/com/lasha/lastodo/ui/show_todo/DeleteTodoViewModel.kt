package com.lasha.lastodo.ui.show_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteTodoViewModel @Inject constructor(private val roomRepository: Repository): ViewModel() {

    fun deleteTodo(todos: Todos, isInternetConnected: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetConnected){
                roomRepository.deleteFromRemote(todos)
            }
            roomRepository.deleteCurrentTodo(todos)
        }
    }
}