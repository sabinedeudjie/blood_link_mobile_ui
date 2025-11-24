package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.AlertStatus
import com.example.bloodlink.data.model.enums.BloodType
import java.time.LocalDateTime
import java.util.UUID

class Alert {

    val alertId: UUID? = null

    val createdAt: LocalDateTime? = null

    val updated_at: LocalDateTime? = null

    val status: AlertStatus? = null

    val quantity: Long? = null

    val compatibleTypes: MutableList<BloodType?>? = null

    val notifications: MutableList<Notification?> = ArrayList<Notification?>()

    val bloodRequest: BloodRequest? = null /*

    @Column(name = "message", nullable = false)
    private String message;
*/
}