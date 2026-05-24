package com.example.cloudmessenging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Este token es el que necesitaremos para enviar mensajes desde el servidor
        Log.d("FCM_TOKEN", "Nuevo token generado: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Manejar mensajes que llegan cuando la app está en primer plano
        Log.d("FCM_MESSAGE", "Mensaje recibido de: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("FCM_MESSAGE", "Cuerpo de la notificación: ${it.body}")
        }

        // Si el mensaje contiene datos adicionales
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM_MESSAGE", "Datos del mensaje: ${remoteMessage.data}")
        }
    }
}
