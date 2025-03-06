package com.example.treninterurbano.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Route
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Iniciar Sesión")
    object Register : Screen("register", "Registro")
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Routes : Screen("routes", "Rutas", Icons.Default.Route)
    object RouteDetail : Screen("route_detail", "Detalle de Ruta")
    object Schedules : Screen("schedules", "Horarios", Icons.Default.AccessTime)
    object Alerts : Screen("alerts", "Alertas", Icons.Default.Notifications)
    object AlertDetail : Screen("alert_detail", "Detalle de Alerta")
    object Qr : Screen("qr", "Mi QR", Icons.Default.QrCode)
    object AccessHistory : Screen("access_history", "Historial", Icons.Default.History)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

