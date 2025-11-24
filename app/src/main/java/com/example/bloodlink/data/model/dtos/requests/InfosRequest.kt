package com.example.bloodlink.data.model.dtos.requests

import com.example.bloodlink.data.model.enums.Gender
import java.time.LocalDate


@JvmRecord
data class InfosRequest(
    val birthdate: LocalDate?,
    val gender: Gender?,
    val weight: Float,
    val emergencyContact: String?
)