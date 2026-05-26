package com.example.cloudmessenging.servicios

import android.util.Log
import com.example.cloudmessenging.datos.MessagingRepo
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Nuevo token generado: $token")
        MessagingRepo.updateToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM_MESSAGE", "Mensaje recibido de: ${remoteMessage.from}")

        val messageBody = remoteMessage.notification?.body ?: remoteMessage.data["message"] ?: "Sin contenido"
        MessagingRepo.updateMessage(messageBody)

        remoteMessage.notification?.let {
            Log.d("FCM_MESSAGE", "Cuerpo de la notificación: ${it.body}")
        }
    }
}
