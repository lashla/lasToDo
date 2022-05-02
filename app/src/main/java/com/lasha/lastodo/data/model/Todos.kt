package com.lasha.lastodo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todos(
    @ColumnInfo(name = "subject")val subject: String,
    @ColumnInfo(name = "content")val contents: String,
    @PrimaryKey @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "photo_path") val photoPath: String?,
    @ColumnInfo(name = "deadline_date") val deadlineDate: String?
)
