package com.example.treninterurbano.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.treninterurbano.data.model.Alert
import com.example.treninterurbano.data.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel() {
    
    suspend fun getActiveAlerts(): Result<List<Alert>> {
        return alertRepository.getActiveAlerts()
    }
}

