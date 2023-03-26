package com.lasha.lastodo.ui.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toTimeString(): String{
    val date = Date(this)
    val format = SimpleDateFormat.getDateTimeInstance()
    return format.format(date)
}