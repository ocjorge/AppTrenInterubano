package com.example.treninterurbano.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.example.treninterurbano.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    suspend fun login(email: String, password: String): Result<UserInfo> {
        return userRepository.signInWithEmail(email, password)
    }
    
    suspend fun register(email: String, password: String, name: String): Result<UserInfo> {
        val signUpResult = userRepository.signUpWithEmail(email, password)
        
        if (signUpResult.isSuccess) {
            val user = signUpResult.getOrNull()
            user?.id?.let { userId ->
                userRepository.updateUserProfile(userId, name)
            }
        }
        
        return signUpResult
    }
}

