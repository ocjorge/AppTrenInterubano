package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val type: AlertType,
    val routeId: String?,
    val stationId: String?,
    val startTime: String,
    val endTime: String?,
    val isActive: Boolean
)

enum class AlertType {
    DELAY, CANCELLATION, MAINTENANCE, SECURITY, INFO
}

