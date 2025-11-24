package com.example.bloodlink.data.model.medicalProfile

import java.time.LocalDateTime
import java.util.UUID

open class HealthQuestions {

    val id: UUID? = null

    val hasTattoosWithinLast6Months = false

    val hasSurgeryWithinLast6_12Months = false

    val hasChronicalIllness = false

    val hasTravelledWithinLast3Months = false

    val hasPiercingWithinLast7Months = false

    val isOnMedication = false

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    fun hasDeferralIssues(): Boolean {
        return hasTattoosWithinLast6Months || hasSurgeryWithinLast6_12Months || hasChronicalIllness || hasPiercingWithinLast7Months || hasTravelledWithinLast3Months || isOnMedication
    }
}