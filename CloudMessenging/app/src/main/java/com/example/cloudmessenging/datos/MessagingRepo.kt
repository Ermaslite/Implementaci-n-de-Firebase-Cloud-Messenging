package com.example.cloudmessenging.datos

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MessagingRepo {
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token.asStateFlow()

    private val _lastMessage = MutableStateFlow<String?>(null)
    val lastMessage: StateFlow<String?> = _lastMessage.asStateFlow()

    fun updateToken(newToken: String) {
        _token.value = newToken
    }

    fun updateMessage(message: String) {
        _lastMessage.value = message
    }
}
