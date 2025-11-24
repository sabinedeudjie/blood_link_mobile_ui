package com.example.bloodlink.retrofit

import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val donorBaseUrl: String = "api/v1/donor"

interface DonorApi {

    @POST("$donorBaseUrl/affiliation-donor-blood-bank/{bankId}")
    suspend fun affiliateDonorToBloodBank(@Path("bankId") bankId: UUID): String?

}