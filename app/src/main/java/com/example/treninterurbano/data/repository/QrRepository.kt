package com.example.treninterurbano.data.repository

import android.graphics.Bitmap
import android.graphics.Color
import com.example.treninterurbano.BuildConfig
import com.example.treninterurbano.data.model.AccessHistory
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.EnumMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrRepository @Inject constructor() {
    
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
    
    suspend fun generateQrCode(userId: String): Result<Bitmap> {
        return try {
            val currentDate = LocalDateTime.now()
            val expirationDate = currentDate.plusMonths(1)
            
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val expirationString = expirationDate.format(formatter)
            
            // Create a unique string that combines user ID and expiration
            val qrContent = "$userId|$expirationString|${generateSecurityHash(userId, expirationString)}"
            
            val bitmap = withContext(Dispatchers.Default) {
                createQrBitmap(qrContent, 512)
            }
            
            Result.success(bitmap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateSecurityHash(userId: String, expirationDate: String): String {
        val contentToHash = "$userId|$expirationDate|${BuildConfig.SUPABASE_ANON_KEY}"
        val bytes = MessageDigest.getInstance("SHA-256").digest(contentToHash.toByteArray())
        return Base64.getEncoder().encodeToString(bytes)
    }
    
    private fun createQrBitmap(content: String, size: Int): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 1
        hints[EncodeHintType.ERROR_CORRECTION] = com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H
        
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        
        return bitmap
    }
    
    suspend fun recordAccess(userId: String, stationId: String, qrCode: String): Result<AccessHistory> {
        return try {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            
            // Verify QR code is valid
            val qrParts = qrCode.split("|")
            val isValid = qrParts.size == 3 && 
                          qrParts[0] == userId && 
                          !isQrExpired(qrParts[1]) &&
                          qrParts[2] == generateSecurityHash(userId, qrParts[1])
            
            val accessRecord = client.from("access_history")
                .insert(mapOf(
                    "userId" to userId,
                    "stationId" to stationId,
                    "timestamp" to timestamp,
                    "isValid" to isValid,
                    "qrCode" to qrCode
                ))
                .select()
                .decodeSingle<AccessHistory>()
            
            Result.success(accessRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun isQrExpired(expirationDateStr: String): Boolean {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val expirationDate = LocalDateTime.parse(expirationDateStr, formatter)
        return LocalDateTime.now().isAfter(expirationDate)
    }
    
    suspend fun getUserAccessHistory(userId: String): Result<List<AccessHistory>> {
        return try {
            val history = client.from("access_history")
                .select()
                .eq("userId", userId)
                .order("timestamp", ascending = false)
                .decodeList<AccessHistory>()
            
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

