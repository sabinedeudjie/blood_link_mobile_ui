package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.data.model.dtos.requests.BloodDemandRequest
import com.example.bloodlink.data.model.enums.RequestStatus
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val bloodRequestBaseUrl: String = "blood-request"

interface BloodRequestApi {

    @POST("$bloodRequestBaseUrl/create")
    suspend fun createRequest(@Body request: BloodDemandRequest): List<BloodRequest>?

    @GET("$bloodRequestBaseUrl/get-pending-bloodRequests-by-bloodbank/id={bankId}/status={status}")
    suspend fun getPendingBloodRequests(
        @Path("bankId") bankId: UUID,
        @Path("status") status: RequestStatus
    ): List<BloodRequest>?

    @POST("$bloodRequestBaseUrl/process-request/{requestId}")
    suspend fun processRequest(@Path("requestId") requestId: UUID): BloodRequest?

}
