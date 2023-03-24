package com.lasha.lastodo.ui.bottom_sheet

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lasha.lastodo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { handleAlarmData(context, it) }
    }

    private fun handleAlarmData(context: Context?, intent: Intent) {

        context?.let {

            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("text")

            createNotificationChannel(context = it)
            if (!title.isNullOrEmpty() && !description.isNullOrEmpty())
            createNotification(
                context,
                title = title,
                description = description
            )
        }
    }

    private fun createNotification(
        context: Context,
        title: String,
        description: String, ) {

        val intent = Intent(context, AddEditDialogFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }

    private fun createNotificationChannel(context: Context) {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESC
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_DESC = "Notifies you before deadline of your task"
        private const val CHANNEL_ID = "CHANNEL_01"
        private const val CHANNEL_NAME = "DEADLINE_NOTIFICATION_CHANNEL"
        private const val REQUEST_CODE = 1
    }
}