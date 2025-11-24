package com.example.bloodlink.data.model.dtos.responses

class ErrorResponse(private val error: String?) {
    val timestamp: Long

    init {
        this.timestamp = System.currentTimeMillis()
    }
}