package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.data.model.metiers.Donor
import java.time.LocalDateTime
import java.util.UUID


class DonationRequest {

    val id: UUID? = null

    val status: RequestStatus? = null

    val createdAt: LocalDateTime? = null

    val donor: Donor? = null

    val bloodBank: BloodBank? = null

    val donation: Donation? = null
}

