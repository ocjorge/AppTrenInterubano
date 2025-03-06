package com.example.treninterurbano.data.repository

import com.example.treninterurbano.BuildConfig
import com.example.treninterurbano.data.model.Schedule
import com.example.treninterurbano.data.model.Weekday
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor() {
    
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
    
    suspend fun getSchedulesForRoute(routeId: String): Result<List<Schedule>> {
        return try {
            val schedules = client.from("schedules")
                .select()
                .eq("routeId", routeId)
                .decodeList<Schedule>()
            Result.success(schedules)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSchedulesForStation(stationId: String): Result<List<Schedule>> {
        return try {
            val schedules = client.from("schedules")
                .select()
                .eq("stationId", stationId)
                .decodeList<Schedule>()
            Result.success(schedules)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSchedulesForRouteAndDay(routeId: String, weekday: Weekday): Result<List<Schedule>> {
        return try {
            val schedules = client.from("schedules")
                .select()
                .eq("routeId", routeId)
                .eq("weekday", weekday.name)
                .decodeList<Schedule>()
            Result.success(schedules)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFirstAndLastDepartures(routeId: String): Result<Pair<String, String>> {
        return try {
            val route = client.from("routes")
                .select("firstDeparture", "lastDeparture")
                .eq("id", routeId)
                .decodeSingle<Map<String, String>>()
            
            val firstDeparture = route["firstDeparture"] ?: ""
            val lastDeparture = route["lastDeparture"] ?: ""
            
            Result.success(Pair(firstDeparture, lastDeparture))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

