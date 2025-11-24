package com.example.bloodlink.data.model.dtos.requests

import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.RequestStatus


data class BloodDemandRequest (
    val recipientType: BloodType?,
    val quantityNeeded: Long?,
    val status: RequestStatus?,
    val bankNames: MutableList<String?>?
)
