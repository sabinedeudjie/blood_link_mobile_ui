package com.example.bloodlink.retrofit

import retrofit2.http.GET

const val bloodBankBaseUrl: String = "v1/bloodbank"

interface BloodBankApi {

    @GET("$bloodBankBaseUrl/initialize-blood-bank-stocks")
    suspend fun initializeBloodBankStocks(): String?

}
