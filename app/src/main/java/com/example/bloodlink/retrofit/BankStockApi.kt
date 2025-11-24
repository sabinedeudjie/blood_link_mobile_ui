package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.appl.BloodBankStock
import com.example.bloodlink.data.model.appl.StockByType
import com.example.bloodlink.data.model.dtos.requests.StockByTypeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

const val bankStockBaseUrl: String = "api/v1/bank-stock"

interface BankStockApi {

    @POST("$bankStockBaseUrl/create")
    suspend fun create(@Body stockByTypeRequest: StockByTypeRequest): BloodBankStock?

    @PUT("$bankStockBaseUrl/update-stock/id={stockId}/quantity={quantity}")
    suspend fun updateQuantity(
        @Path("stockId") stockId: UUID,
        @Path("quantity") quantity: Long
    ): StockByType?

    @GET("$bankStockBaseUrl/update-total-quantity")
    suspend fun updateTotalQuantity(): Long?

}