package com.example.bloodlink.data.model.appl

import java.time.LocalDateTime
import java.util.UUID


class BloodBankStock {

    val id: UUID? = null

    var totalQuantity: Long = 0
    val stockByTypeList: MutableSet<StockByType?> = HashSet<StockByType?>()

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    fun addStockByType(type: StockByType?) {
        this.stockByTypeList.add(type)
    }

}

