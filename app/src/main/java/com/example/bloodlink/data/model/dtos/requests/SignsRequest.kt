package com.example.bloodlink.data.model.dtos.requests

@JvmRecord
data class SignsRequest(
    val hemoglobinLevel: Float,

    val bodyTemperature: Float,

    val pulseRate: Float
)