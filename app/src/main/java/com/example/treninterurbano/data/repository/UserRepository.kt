package com.example.treninterurbano.data.repository

import com.example.treninterurbano.BuildConfig
import com.example.treninterurbano.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    
    suspend fun signUpWithEmail(email: String, password: String): Result<UserInfo> {
        return try {
            val user = client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signInWithEmail(email: String, password: String): Result<UserInfo> {
        return try {
            val user = client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUser(): Result<UserInfo?> {
        return try {
            val user = client.auth.currentUserOrNull()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProfile(userId: String, name: String): Result<User> {
        return try {
            val updatedUser = client.from("users")
                .update({ "name" to name })
                .eq("id", userId)
                .select()
                .decodeSingle<User>()
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshSession(): Result<Unit> {
        return try {
            client.auth.refreshSession()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

