package com.example.bloodlink.data.model.medicalProfile

import java.time.LocalDateTime
import java.util.UUID

class VitalSigns {

    val id: UUID? = null

    val hemoglobinLevel = 0f

    val bodyTemperature = 0f

    val pulseRate = 0f

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    fun meetDonationCriteria(): Boolean {
        if ((hemoglobinLevel >= 12.0) && (bodyTemperature <= 38) && (bodyTemperature >= 37) && (pulseRate <= 100) && (pulseRate >= 50)) {
            return true
        } else return false
    }
}