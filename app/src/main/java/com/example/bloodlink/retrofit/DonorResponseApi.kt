package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.appl.DonorResponse
import com.example.bloodlink.data.model.dtos.requests.ResponseRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val donorResponseBaseUrl: String = "api/v1/donor-response"

interface DonorResponseApi {

    @POST("$donorResponseBaseUrl/create-donor-response/{notifID}")
    suspend fun createDonorResponse(
        @Path("notifID") notifID: UUID,
        @Body request: ResponseRequest
    ): DonorResponse?

}