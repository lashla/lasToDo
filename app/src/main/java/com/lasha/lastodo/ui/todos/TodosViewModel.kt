package com.lasha.lastodo.ui.todos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val todosData = MutableLiveData<List<Todo>>()
    private val ioDispatcher = Dispatchers.IO

    init {
        viewModelScope.launch(ioDispatcher) {
            todosData.postValue(repository.getAllTodos())
        }
    }

    fun getSortedByDeadline() {
        viewModelScope.launch(ioDispatcher) {
            repository.getSortedDeadline().let {
                if (it.isNotEmpty()) {
                    todosData.postValue(it)
                }
            }
        }
    }

    fun getSortedByDate() {
        viewModelScope.launch(ioDispatcher) {
            repository.getSortedTodos().let {
                if (it.isNotEmpty()) {
                    todosData.postValue(it)
                }
            }
        }
    }
}