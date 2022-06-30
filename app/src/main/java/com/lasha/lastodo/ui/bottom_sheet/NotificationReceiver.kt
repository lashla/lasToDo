package com.lasha.lastodo.ui.bottom_sheet

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.lasha.lastodo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification: Notification = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.drawable.ic_svg1)
            .setContentTitle(intent!!.getStringExtra("title"))
            .setContentText(intent.getStringExtra("text"))
            .setAutoCancel(true)
            .build()
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(42, notification)
    }
}