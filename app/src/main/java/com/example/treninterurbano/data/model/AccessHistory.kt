package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessHistory(
    val id: String,
    val userId: String,
    val stationId: String,
    val timestamp: String,
    val isValid: Boolean,
    val qrCode: String
)

