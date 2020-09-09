package com.javiermendonca.atomicdropsnotifier.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.javiermendonca.atomicdropsnotifier.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create a Notification that is shown as a heads-up notification if possible.
 *
 * @param title Title shown on the notification
 * @param message Message shown on the notification
 * @param context Context needed to create the notification channel and fetch resources
 * @param notificationChannelId Id of the notification channel to post the notification on
 */
fun makeStatusNotification(
    title: String?,
    message: String?,
    context: Context,
    @StringRes notificationChannelId: Int = R.string.notifications_drops_channel_id
) = with(context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.run {
            createNotificationChannel(NotificationChannel(
                getString(R.string.notifications_drops_channel_id),
                getString(R.string.notifications_drops_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notifications_drops_description)
            })
            createNotificationChannel(NotificationChannel(
                getString(R.string.notifications_health_channel_id),
                getString(R.string.notifications_health_channel),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notifications_health_description)
            })
        }
    }

    val notificationId = if (notificationChannelId == R.string.notifications_drops_channel_id) {
        R.integer.notification_drop_id
    } else {
        R.integer.notification_health_id
    }

    NotificationManagerCompat.from(this).notify(
        resources.getInteger(notificationId),
        NotificationCompat.Builder(this, getString(notificationChannelId))
            .setSmallIcon(R.drawable.ic_atomic_bell)
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

var SIMPLE_FORMAT_DATE = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
