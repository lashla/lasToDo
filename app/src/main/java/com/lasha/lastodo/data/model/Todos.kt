package com.lasha.lastodo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todos")
data class Todos(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "subject")var subject: String = "",
    @ColumnInfo(name = "content")var contents: String = "",
    @ColumnInfo(name = "date")var date: String = "",
    @ColumnInfo(name = "photo_path") var photoPath: String? = "",
    @ColumnInfo(name = "deadline_date") var deadlineDate: String? = ""
): Parcelable
