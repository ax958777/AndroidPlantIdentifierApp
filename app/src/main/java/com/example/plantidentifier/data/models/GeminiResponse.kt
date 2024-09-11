package com.example.plantidentifier.data.models

data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate (
    val content: Content,
    val finishReason: String,
    val index: Long,
    val safetyRatings: List<SafetyRating>
)



data class SafetyRating (
    val category: String,
    val probability: String
)

data class UsageMetadata (
    val promptTokenCount: Long,
    val candidatesTokenCount: Long,
    val totalTokenCount: Long
)