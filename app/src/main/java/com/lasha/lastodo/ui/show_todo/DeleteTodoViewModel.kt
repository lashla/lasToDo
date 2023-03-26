package com.lasha.lastodo.ui.show_todo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.domain.repository.Repository
import com.lasha.lastodo.domain.worker.DeletionWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeleteTodoViewModel @Inject constructor(private val roomRepository: Repository) :
    ViewModel() {

    fun deleteTodo(todo: Todo, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            val workerConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val initDeleteWorker = OneTimeWorkRequestBuilder<DeletionWorker>()
                .setConstraints(workerConstraints)
                .setInputData(Data.Builder()
                    .putString(TODO, serializeToJson(todo))
                    .build())
                .build()
            WorkManager.getInstance(context).enqueue(initDeleteWorker)
            roomRepository.deleteLocalTodo(todo)
        }
    }

    private fun serializeToJson(todo: Todo): String {
        val gson = Gson()
        return gson.toJson(todo)
    }

    companion object{
        const val TODO = "TODO"
    }
}