package com.mili.housead.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mili.housead.MainActivity
import com.mili.housead.NotificationProperty
import com.mili.housead.R

private const val NOTIFICATION_ID = 45665654
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(
    notificationProperty: NotificationProperty
    , applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

//    val smallIconBitmap: Bitmap = if (notificationProperty.smallIconUrl != null) {
//        bitmapImageWithGlide(notificationProperty.smallIconUrl, applicationContext)
//    } else {
//        BitmapFactory.decodeResource(
//            applicationContext.resources,
//            R.drawable.ic_home_black_24dp
//        )
//    }

    val largeIconBitmap: Bitmap = if (notificationProperty.largeIconUrl != null) {
        bitmapImageWithGlide(
            notificationProperty.largeIconUrl,
            applicationContext
        )
    } else {
        BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.house
        )
    }

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.house_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_stat_home)
        .setContentTitle(notificationProperty.title)
        .setContentText(notificationProperty.messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(largeIconBitmap)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notificationProperty.bigBody?.let {
        val bigTextStyle = NotificationCompat.BigTextStyle().bigText(it)
        builder.setStyle(bigTextStyle)
    }

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotification() {
    cancelAll()
}

fun bitmapImageWithGlide(
    imageUrl: String,
    applicationContext: Context
): Bitmap {
    val largeIconUri = imageUrl.toUri().buildUpon().scheme("https").build()
    val futureTarget = Glide.with(applicationContext)
        .asBitmap()
        .load(largeIconUri)
        .apply(RequestOptions.circleCropTransform())
        .submit()
    val bitmap = futureTarget.get()
    Glide.with(applicationContext).clear(futureTarget)
    return bitmap
}
