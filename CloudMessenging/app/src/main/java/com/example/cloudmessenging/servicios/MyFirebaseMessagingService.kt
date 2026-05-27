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

        // Extraemos título y cuerpo tanto de la notificación como de los datos (data)
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Notificación"
        val body = remoteMessage.data["message"] ?: remoteMessage.notification?.body ?: "Sin contenido"
        
        // Añadimos el mensaje con su título al historial
        MessagingRepo.addMessage(title, body)
    }
}
