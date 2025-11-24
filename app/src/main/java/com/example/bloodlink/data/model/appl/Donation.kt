package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.DonationStatus
import java.time.LocalDateTime
import java.util.UUID


class Donation {

    val donationId: UUID? = null

    val bloodType: BloodType? = null

    val unitsGave = 0

    val status: DonationStatus? = null

    val isValidated = false

    val donationDate: LocalDateTime? = null
}

