package com.example.treninterurbano.ui.screens.qr

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.treninterurbano.data.repository.QrRepository
import com.example.treninterurbano.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(
    private val qrRepository: QrRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    suspend fun generateQrCode(): Result<Pair<Bitmap, LocalDateTime>> {
        val currentUserResult = userRepository.getCurrentUser()
        
        if (currentUserResult.isSuccess) {
            val user = currentUserResult.getOrNull()
            user?.id?.let { userId ->
                val qrResult = qrRepository.generateQrCode(userId)
                
                if (qrResult.isSuccess) {
                    val qrBitmap = qrResult.getOrNull()
                    val expirationDate = LocalDateTime.now().plusMonths(1)
                    
                    return qrBitmap?.let {
                        Result.success(Pair(it, expirationDate))
                    } ?: Result.failure(Exception("Error generando QR"))
                }
                
                return Result.failure(Exception("Error generando QR"))
            }
            
            return Result.failure(Exception("Usuario no identificado"))
        }
        
        return Result.failure(currentUserResult.exceptionOrNull() ?: Exception("Error obteniendo usuario"))
    }
}

