package com.example.treninterurbano.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.treninterurbano.data.model.Alert
import com.example.treninterurbano.data.model.AlertType
import com.example.treninterurbano.ui.theme.trainError
import com.example.treninterurbano.ui.theme.trainInfo
import com.example.treninterurbano.ui.theme.trainWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertItem(
    alert: Alert,
    onClick: () -> Unit
) {
    val (iconVector, iconColor) = when (alert.type) {
        AlertType.DELAY, AlertType.CANCELLATION -> 
            Pair(Icons.Default.Warning, trainWarning)
        AlertType.MAINTENANCE, AlertType.SECURITY -> 
            Pair(Icons.Default.Warning, trainError)
        AlertType.INFO -> 
            Pair(Icons.Default.Info, trainInfo)
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = alert.type.name,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

