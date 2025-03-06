package com.example.treninterurbano.data.repository

import com.example.treninterurbano.BuildConfig
import com.example.treninterurbano.data.model.Alert
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepository @Inject constructor() {
    
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
    
    suspend fun getActiveAlerts(): Result<List<Alert>> {
        return try {
            val alerts = client.from("alerts")
                .select()
                .eq("isActive", true)
                .decodeList<Alert>()
            
            Result.success(alerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAlertsForRoute(routeId: String): Result<List<Alert>> {
        return try {
            val alerts = client.from("alerts")
                .select()
                .eq("routeId", routeId)
                .eq("isActive", true)
                .decodeList<Alert>()
            
            Result.success(alerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAlertsForStation(stationId: String): Result<List<Alert>> {
        return try {
            val alerts = client.from("alerts")
                .select()
                .eq("stationId", stationId)
                .eq("isActive", true)
                .decodeList<Alert>()
            
            Result.success(alerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAlert(alertId: String): Result<Alert> {
        return try {
            val alert = client.from("alerts")
                .select()
                .eq("id", alertId)
                .decodeSingle<Alert>()
            
            Result.success(alert)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

