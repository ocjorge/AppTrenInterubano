package com.example.treninterurbano.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.treninterurbano.data.model.Alert
import com.example.treninterurbano.ui.components.AlertItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRoutes: () -> Unit,
    onNavigateToSchedules: () -> Unit,
    onNavigateToQr: () -> Unit,
    onNavigateToAlert: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var activeAlerts by remember { mutableStateOf<List<Alert>>(emptyList()) }
    
    LaunchedEffect(key1 = Unit) {
        val alertsResult = viewModel.getActiveAlerts()
        if (alertsResult.isSuccess) {
            activeAlerts = alertsResult.getOrNull() ?: emptyList()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tren Interurbano") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "¿Qué deseas hacer hoy?",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeActionCard(
                        title = "Rutas",
                        icon = Icons.Default.Route,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToRoutes
                    )
                    
                    HomeActionCard(
                        title = "Horarios",
                        icon = Icons.Default.AccessTime,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToSchedules
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                HomeActionCard(
                    title = "Mi código QR",
                    icon = Icons.Default.QrCode,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToQr
                )
            }
            
            if (activeAlerts.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Alertas Activas",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                items(activeAlerts) { alert ->
                    AlertItem(
                        alert = alert,
                        onClick = { onNavigateToAlert(alert.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.height(48.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = title)
        }
    }
}

