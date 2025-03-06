package com.example.treninterurbano.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.treninterurbano.data.model.AccessHistory
import com.example.treninterurbano.ui.theme.trainError
import com.example.treninterurbano.ui.theme.trainSuccess
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AccessHistoryItem(
    accessHistory: AccessHistory,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Valid/Invalid icon
            Icon(
                imageVector = if (accessHistory.isValid) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (accessHistory.isValid) "Acceso válido" else "Acceso inválido",
                tint = if (accessHistory.isValid) trainSuccess else trainError,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Format date and time
                val dateTime = try {
                    LocalDateTime.parse(accessHistory.timestamp)
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
                } catch (e: Exception) {
                    accessHistory.timestamp
                }
                
                Text(
                    text = "Estación: ${accessHistory.stationId}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = dateTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Text(
                    text = if (accessHistory.isValid) "Acceso válido" else "Acceso inválido",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (accessHistory.isValid) trainSuccess else trainError
                )
            }
        }
    }
}

