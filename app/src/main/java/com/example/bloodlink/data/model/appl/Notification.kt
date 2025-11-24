package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.AlertStatus
import com.example.bloodlink.data.model.metiers.Donor
import java.time.LocalDateTime
import java.util.List
import java.util.UUID


class Notification {

    val notification_id: UUID? = null

    val alertStatus: AlertStatus? = null

    val createdAt: LocalDateTime? = null

    val alert: Alert? = null

    val donor: Donor? = null

    val donorResponse: DonorResponse? = null


    fun callback() {
    }
}