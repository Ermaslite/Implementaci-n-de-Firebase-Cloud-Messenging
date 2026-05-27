package com.example.cloudmessenging.ui.mensajes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cloudmessenging.datos.MessageData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(viewModel: MessagingViewModel) {
    val token by viewModel.token.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FCM Historial") },
                actions = {
                    if (messages.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearHistory() }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Limpiar historial")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VpnKey, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Tu Token ID", fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(8.dp))
                            androidx.compose.foundation.text.selection.SelectionContainer {
                                Text(
                                    text = token.ifEmpty { "Obteniendo token..." },
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { copyToClipboard(context, token) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = token.isNotEmpty()
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Copiar Token")
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Historial de Mensajes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Notifications, 
                                    contentDescription = null, 
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Sin mensajes recibidos", color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }

                // Lista de Historial con Título en negrita
                items(messages) { msg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Message, 
                                contentDescription = null, 
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = msg.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = msg.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    if (text.isEmpty()) return
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("FCM Token", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Token copiado", Toast.LENGTH_SHORT).show()
}
