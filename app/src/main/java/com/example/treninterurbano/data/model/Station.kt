package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Station(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val services: List<String> = emptyList()
)

