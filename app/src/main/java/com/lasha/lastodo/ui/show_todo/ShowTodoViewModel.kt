package com.lasha.lastodo.ui.show_todo

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowTodoViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun downloadPicture(fileName: String, resolver: ContentResolver, todos: Todos){
        viewModelScope.launch(Dispatchers.IO) {
           val uri = repository.downloadFile(fileName, resolver)
            repository.updateCurrentTodo(Todos(todos.id, todos.subject, todos.contents, todos.date, uri.toString(), todos.deadlineDate, todos.photoLink))
        }
    }
}