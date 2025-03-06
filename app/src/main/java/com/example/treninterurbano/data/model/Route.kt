package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val id: String,
    val name: String,
    val color: String,
    val stations: List<String>,
    val type: RouteType,
    val firstDeparture: String,
    val lastDeparture: String
)

enum class RouteType {
    MAIN, FEEDER
}

