package com.javiermendonca.atomicassetsnotifier.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.javiermendonca.atomicassetsnotifier.R

/**
 * Create a Notification that is shown as a heads-up notification if possible.
 *
 * @param title Title shown on the notification
 * @param message Message shown on the notification
 * @param context Context needed to create the notification channel and fetch resources
 */
fun makeStatusNotification(title: String?, message: String?, context: Context) = with(context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.run {
            createNotificationChannel(NotificationChannel(
                getString(R.string.notifications_channel_id),
                getString(R.string.notifications_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notifications_description)
            })
        }
    }

    NotificationManagerCompat.from(this).notify(
        resources.getInteger(R.integer.notification_id),
        NotificationCompat.Builder(this, getString(R.string.notifications_channel_id))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setSummaryText(getString(R.string.notifications_new_drop_message_summary))
                    .bigText(message)
            )
            .build()
    )
}