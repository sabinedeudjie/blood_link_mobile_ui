package com.example.bloodlink.data.model.dtos.responses

@JvmRecord
data class BloodDemandResponse(
    val bloodBankNames: MutableList<String?>?,
    val quantity: Long?,
    val doctorName: String?
)