package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.data.model.metiers.Doctor
import java.time.LocalDateTime
import java.util.List
import java.util.UUID


class BloodRequest {

    val id: UUID? = null

    val groupRequestId: UUID? = null

    val recipientType: BloodType? = null

    val quantityNeeded: Long? = null

    val status: RequestStatus? = null

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    //Bidirectional (Many - Many) relationship between Doctor and Blood bank
    val doctor: Doctor? = null

    val bloodBank: BloodBank? = null

    val alert: Alert? = null

    fun afterAll() {
    }
}
