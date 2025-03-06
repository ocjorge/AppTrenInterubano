package com.example.treninterurbano.ui.screens.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScreen(
    viewModel: QrViewModel = hiltViewModel()
) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expirationDate by remember { mutableStateOf<LocalDateTime?>(null) }
    
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(key1 = Unit) {
        loadQrCode(viewModel) { bitmap, expiration ->
            qrBitmap = bitmap
            expirationDate = expiration
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Código QR") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tu Acceso Personal",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Presenta este código en las estaciones",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(120.dp),
                            strokeWidth = 4.dp
                        )
                    } else if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "Error al cargar el código QR",
                            color = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    loadQrCode(viewModel) { bitmap, expiration ->
                                        qrBitmap = bitmap
                                        expirationDate = expiration
                                        isLoading = false
                                    }
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    } else {
                        qrBitmap?.let { bitmap ->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Código QR",
                                    modifier = Modifier.size(240.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            expirationDate?.let { expDate ->
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val now = LocalDateTime.now()
                                val daysRemaining = ChronoUnit.DAYS.between(now, expDate)
                                
                                Text(
                                    text = "Válido hasta: ${expDate.format(formatter)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Text(
                                    text = "($daysRemaining días restantes)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (daysRemaining < 7) 
                                        MaterialTheme.colorScheme.error 
                                    else 
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Este código QR es personal e intransferible. " +
                       "Se renueva automáticamente cada mes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private suspend fun loadQrCode(
    viewModel: QrViewModel,
    onResult: (Bitmap?, LocalDateTime?) -> Unit
) {
    val result = viewModel.generateQrCode()
    
    if (result.isSuccess) {
        val qrData = result.getOrNull()
        onResult(qrData?.first, qrData?.second)
    } else {
        onResult(null, null)
    }
}

