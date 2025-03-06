package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val id: String,
    val routeId: String,
    val stationId: String,
    val weekday: Weekday,
    val departureTimes: List<String>
)

enum class Weekday {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

