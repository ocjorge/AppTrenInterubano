package com.example.treninterurbano.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String = "",
    val createdAt: String = "",
    val lastAccess: String = ""
)

