package com.example.cloudmessenging.datos

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MessageData(
    val title: String,
    val body: String
)

object MessagingRepo {
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageData>>(emptyList())
    val messages: StateFlow<List<MessageData>> = _messages.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun updateToken(newToken: String) {
        _token.value = newToken
    }

    fun addMessage(title: String, body: String) {
        _messages.update { currentList ->
            listOf(MessageData(title, body)) + currentList
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        _isRefreshing.value = refreshing
    }

    fun clearHistory() {
        _messages.value = emptyList()
    }
}
