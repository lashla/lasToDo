package com.lasha.lastodo.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lasha.lastodo.data.model.Todos

@Database(entities = [Todos::class], version = 3, exportSchema = false)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todosDao(): TodosDao
    companion object {
        val DATABASE_NAME = "todo database"
    }

}