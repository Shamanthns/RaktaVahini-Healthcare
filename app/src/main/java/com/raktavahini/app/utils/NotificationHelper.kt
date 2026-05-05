package com.raktavahini.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.raktavahini.app.R

object NotificationHelper {

    private const val CHANNEL_ID = "rakta_vahini_channel"
    private const val CHANNEL_NAME = "Rakta-Vahini"
    private const val CHANNEL_DESC = "Donation notifications"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESC
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun sendThankYouNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_blood_drop)
            .setContentTitle("Thank You, Hero! 🩸")
            .setContentText("Thank you for saving a life! Your donation makes a difference.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Thank you for saving a life! Your donation has been logged. You are a true hero of humanity. Remember, you can donate again after 90 days.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        manager.notify(1001, notification)
    }
}
