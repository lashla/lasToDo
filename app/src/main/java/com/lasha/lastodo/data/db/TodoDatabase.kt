package com.lasha.lastodo.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.lasha.lastodo.data.model.Todo

@Database(entities = [Todo::class], version = 6,
    autoMigrations = [AutoMigration(from = 5, to = 6)]
)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todosDao(): TodosDao
    companion object {
        const val DATABASE_NAME = "todo database"
    }

}