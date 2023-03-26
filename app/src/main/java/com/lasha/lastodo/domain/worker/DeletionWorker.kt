package com.lasha.lastodo.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.domain.repository.Repository
import javax.inject.Inject

class DeletionWorker @Inject constructor(
    private val repository: Repository,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val jsonString = inputData.getString(TODO)
        val todoItem = deserializeFromJson(jsonString)

        return if (todoItem == null){
            Result.retry()
        } else {
            repository.deleteFromRemote(todoItem)
            Result.success()
        }
    }

    private fun deserializeFromJson(jsonString: String?): Todo? {
        val gson = Gson()
        return gson.fromJson(jsonString, Todo::class.java)
    }

    companion object{
        const val TODO = "TODO"
    }
}

