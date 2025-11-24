package com.example.bloodlink.data.model.enums

enum class AlertResponse(val value: Boolean) {
    APPROVE(true),
    DECLINE(false);

    val response: Boolean = false

    companion object {
        fun findByResponse(response: Boolean): AlertResponse {
            for (alertResponse in AlertResponse.entries) {
                if (alertResponse.response == response) {
                    return alertResponse
                }
            }
            throw IllegalArgumentException("Invalid Alert response " + response)
        }
    }
}