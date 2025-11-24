package com.example.bloodlink.data.model.dtos.requests

import com.example.bloodlink.data.model.enums.BloodType


class StockByTypeRequest (
    val bloodType: BloodType?,
    val quantity: Long?
)