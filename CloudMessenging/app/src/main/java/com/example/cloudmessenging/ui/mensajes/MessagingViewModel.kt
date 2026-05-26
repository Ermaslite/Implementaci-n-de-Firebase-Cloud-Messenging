package com.example.cloudmessenging.ui.mensajes

import androidx.lifecycle.ViewModel
import com.example.cloudmessenging.datos.MessagingRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.StateFlow

class MessagingViewModel : ViewModel() {

    val token: StateFlow<String> = MessagingRepo.token
    val lastMessage: StateFlow<String?> = MessagingRepo.lastMessage

    init {
        fetchCurrentToken()
    }

    private fun fetchCurrentToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                MessagingRepo.updateToken(token)
            }
        }
    }
}
