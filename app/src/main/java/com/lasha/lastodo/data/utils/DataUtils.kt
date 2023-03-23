package com.lasha.lastodo.data.utils

import com.lasha.lastodo.data.model.Todo

fun checkIfLocalItemsUpToDate(remoteItem: Todo, localItem: Todo): Boolean {
    if (localItem.id == remoteItem.id && localItem.date < remoteItem.date) {
        return true
    }
    return false
}

fun checkIfRemoteItemsUpToDate(remoteItem: Todo, localItem: Todo): Boolean {
    if (localItem.id == remoteItem.id && localItem.date > remoteItem.date) {
        return true
    }
    return false
}
