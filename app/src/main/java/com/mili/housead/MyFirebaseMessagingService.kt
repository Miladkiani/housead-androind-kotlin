package com.mili.housead

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mili.housead.utilities.sendNotification

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TOPIC = "all"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        //Check if message contains a data payload.
        remoteMessage.data.let {
            try {
                val title: String = remoteMessage.data["title"].toString()
                val messageBody: String = remoteMessage.data["message_body"].toString()
                val bigBody: String? = remoteMessage.data["big_body"]
                val smallIconUrl: String? = remoteMessage.data["small_icon"]
                val largeIconUrl: String? = remoteMessage.data["large_icon"]
                sendNotification(
                    NotificationProperty(
                        title, messageBody, bigBody,
                        smallIconUrl, largeIconUrl
                    )
                )
            } catch (ex: Exception) {

            }

        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)


        //sendRegistrationToServer()
        subscribeTopic()
    }

    private fun subscribeTopic() {
        // [Start subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            .addOnCompleteListener {
//                var msg = getString(R.string.message_subscribed)
//                if (!task.isSuccessful) {
//                    msg = getString(R.string.message_subscribe_failed)
//                }
//                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
//
            }
        // [End subscribe_topics]
    }

    private fun sendNotification(
        notificationProperty: NotificationProperty
    ) {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(
            notificationProperty
            , applicationContext
        )
    }


}

data class NotificationProperty(
    val title: String,
    val messageBody: String,
    val bigBody: String?,
    val smallIconUrl: String?,
    val largeIconUrl: String?
)