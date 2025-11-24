package com.example.bloodlink.data.model.dtos.requests

import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.data.model.enums.AlertStatus


data class AlertRequest (
    val alertStatus: AlertStatus,
    val request: BloodRequest
)