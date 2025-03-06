package com.example.treninterurbano.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.treninterurbano.ui.navigation.BottomNavigationBar
import com.example.treninterurbano.ui.navigation.Screen
import com.example.treninterurbano.ui.screens.access_history.AccessHistoryScreen
import com.example.treninterurbano.ui.screens.alerts.AlertDetailScreen
import com.example.treninterurbano.ui.screens.alerts.AlertsScreen
import com.example.treninterurbano.ui.screens.auth.LoginScreen
import com.example.treninterurbano.ui.screens.auth.RegisterScreen
import com.example.treninterurbano.ui.screens.home.HomeScreen
import com.example.treninterurbano.ui.screens.profile.ProfileScreen
import com.example.treninterurbano.ui.screens.qr.QrScreen
import com.example.treninterurbano.ui.screens.routes.RouteDetailScreen
import com.example.treninterurbano.ui.screens.routes.RoutesScreen
import com.example.treninterurbano.ui.screens.schedules.ScheduleScreen
import com.example.treninterurbano.ui.viewmodel.MainViewModel

@Composable
fun TrenInterurbanoApp(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    
    val isLoggedIn = viewModel.isUserLoggedIn.value
    
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    val showBottomBar = when (currentRoute) {
        Screen.Login.route, Screen.Register.route, 
        Screen.AlertDetail.route + "/{alertId}" -> false
        else -> isLoggedIn
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth screens
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    },
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            
            // Main screens
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToRoutes = {
                        navController.navigate(Screen.Routes.route)
                    },
                    onNavigateToSchedules = {
                        navController.navigate(Screen.Schedules.route)
                    },
                    onNavigateToQr = {
                        navController.navigate(Screen.Qr.route)
                    },
                    onNavigateToAlert = { alertId ->
                        navController.navigate(Screen.AlertDetail.route + "/$alertId")
                    }
                )
            }
            
            composable(Screen.Routes.route) {
                RoutesScreen(
                    onRouteSelected = { routeId ->
                        navController.navigate(Screen.RouteDetail.route + "/$routeId")
                    }
                )
            }
            
            composable(
                route = Screen.RouteDetail.route + "/{routeId}",
                arguments = listOf(navArgument("routeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
                RouteDetailScreen(
                    routeId = routeId,
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Schedules.route) {
                ScheduleScreen()
            }
            
            composable(Screen.Alerts.route) {
                AlertsScreen(
                    onAlertSelected = { alertId ->
                        navController.navigate(Screen.AlertDetail.route + "/$alertId")
                    }
                )
            }
            
            composable(
                route = Screen.AlertDetail.route + "/{alertId}",
                arguments = listOf(navArgument("alertId") { type = NavType.StringType })
            ) { backStackEntry ->
                val alertId = backStackEntry.arguments?.getString("alertId") ?: ""
                AlertDetailScreen(
                    alertId = alertId,
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.Qr.route) {
                QrScreen()
            }
            
            composable(Screen.AccessHistory.route) {
                AccessHistoryScreen()
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

