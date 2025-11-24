package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.appl.Donation
import com.example.bloodlink.data.model.appl.DonationRequest
import com.example.bloodlink.data.model.dtos.requests.DonationDemandRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val donationRequestBaseUrl: String = "api/v1/donation-request"

interface DonationRequestApi {

    @POST("$donationRequestBaseUrl/create-donation-request/{notifId}")
    suspend fun createDonationRequest(
        @Path("notifId") notifId: UUID,
        @Body demandRequest: DonationDemandRequest
    ): DonationRequest?

    @POST("$donationRequestBaseUrl/process-donation-request/{donationRequestId}")
    suspend fun processDonationRequest(
        @Path("donationRequestId") donationRequestId: UUID
    ): Donation?

}