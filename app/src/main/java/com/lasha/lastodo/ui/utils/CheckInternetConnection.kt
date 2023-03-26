package com.lasha.lastodo.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object CheckInternetConnection {
    fun connectivityStatus(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        } else {
            val activeNetwork = connectivityManager.isDefaultNetworkActive
            if (activeNetwork) {
                return false
            }
        }
        return false
    }
}