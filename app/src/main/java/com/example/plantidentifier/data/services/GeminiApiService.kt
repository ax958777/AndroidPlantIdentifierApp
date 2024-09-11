// File: app/src/main/java/com/example/plantidentifier/data/services/GeminiApiService.kt

package com.example.plantidentifier.data.services


import com.example.plantidentifier.data.models.GeminiRequest
import com.example.plantidentifier.data.models.GeminiResponse
import com.example.plantidentifier.data.models.PlantInfo
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent?key=")
    //suspend fun identifyPlant(@Body request: GeminiRequest): PlantInfo
    suspend fun identifyPlant(@Body request: GeminiRequest): GeminiResponse
}