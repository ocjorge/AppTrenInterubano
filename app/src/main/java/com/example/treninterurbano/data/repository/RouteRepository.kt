package com.example.treninterurbano.data.repository

import com.example.treninterurbano.BuildConfig
import com.example.treninterurbano.data.model.Route
import com.example.treninterurbano.data.model.Station
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteRepository @Inject constructor() {
    
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
    
    suspend fun getAllRoutes(): Result<List<Route>> {
        return try {
            val routes = client.from("routes")
                .select()
                .decodeList<Route>()
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getMainRoutes(): Result<List<Route>> {
        return try {
            val routes = client.from("routes")
                .select()
                .eq("type", "MAIN")
                .decodeList<Route>()
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFeederRoutes(): Result<List<Route>> {
        return try {
            val routes = client.from("routes")
                .select()
                .eq("type", "FEEDER")
                .decodeList<Route>()
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRouteById(routeId: String): Result<Route> {
        return try {
            val route = client.from("routes")
                .select()
                .eq("id", routeId)
                .decodeSingle<Route>()
            Result.success(route)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllStations(): Result<List<Station>> {
        return try {
            val stations = client.from("stations")
                .select()
                .decodeList<Station>()
            Result.success(stations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getStationsForRoute(routeId: String): Result<List<Station>> {
        return try {
            val route = getRouteById(routeId).getOrThrow()
            val stations = client.from("stations")
                .select()
                .inFilter("id", route.stations)
                .decodeList<Station>()
            Result.success(stations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

