package com.example.treninterurbano.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treninterurbano.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _isUserLoggedIn = mutableStateOf(false)
    val isUserLoggedIn: State<Boolean> = _isUserLoggedIn
    
    init {
        checkUserLoggedIn()
    }
    
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val result = userRepository.getCurrentUser()
            _isUserLoggedIn.value = result.isSuccess && result.getOrNull() != null
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            userRepository.signOut()
            _isUserLoggedIn.value = false
        }
    }
}

