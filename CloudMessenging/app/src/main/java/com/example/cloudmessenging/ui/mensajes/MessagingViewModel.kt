package com.example.cloudmessenging.ui.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudmessenging.datos.MessageData
import com.example.cloudmessenging.datos.MessagingRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessagingViewModel : ViewModel() {

    val token: StateFlow<String> = MessagingRepo.token
    val messages: StateFlow<List<MessageData>> = MessagingRepo.messages
    val isRefreshing: StateFlow<Boolean> = MessagingRepo.isRefreshing

    init {
        fetchCurrentToken()
    }

    fun fetchCurrentToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                MessagingRepo.updateToken(token)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            MessagingRepo.setRefreshing(true)
            fetchCurrentToken()
            delay(1000)
            MessagingRepo.setRefreshing(false)
        }
    }

    fun clearHistory() {
        MessagingRepo.clearHistory()
    }
}
