package com.lasha.lastodo.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todos")
data class Todos(
    @PrimaryKey(autoGenerate = true)val id: Int,
    @ColumnInfo(name = "subject")val subject: String,
    @ColumnInfo(name = "content")val contents: String,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "photo_path") val photoPath: Uri?,
    @ColumnInfo(name = "deadline_date") val deadlineDate: String?
): Parcelable{
    constructor(subject: String, contents: String, date: String, photoPath: Uri?, deadlineDate: String?) : this(0, subject, contents, date, photoPath, deadlineDate)
}
