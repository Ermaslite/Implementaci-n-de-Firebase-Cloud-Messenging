package com.example.cloudmessenging

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.cloudmessenging.datos.MessagingRepo
import com.example.cloudmessenging.ui.mensajes.MessagingScreen
import com.example.cloudmessenging.ui.mensajes.MessagingViewModel
import com.example.cloudmessenging.ui.theme.CloudMessengingTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MessagingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            CloudMessengingTheme {
                NotificationPermissionHandler()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MessagingScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.extras?.let { extras ->
            val title = extras.getString("gcm.notification.title") ?: extras.getString("title") ?: "Notificación"
            val body = extras.getString("message") ?: extras.getString("gcm.notification.body")
            
            if (body != null) {
                MessagingRepo.addMessage(title, body)
            }
        }
    }
}

@Composable
fun NotificationPermissionHandler() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
