package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.BloodType
import java.time.LocalDateTime
import java.util.UUID


class StockByType {
    val typeId: UUID? = null

    val bloodType: BloodType? = null

    var quantity: Long? = null // I've changed the type from Integer to Long

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    fun upgradeQuantity(quantity: Long) {
        this.quantity = (this.quantity ?: 0L) + quantity
    }
}