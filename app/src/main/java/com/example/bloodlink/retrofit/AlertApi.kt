package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.appl.Alert
import com.example.bloodlink.data.model.enums.BloodType
import retrofit2.http.POST
import retrofit2.http.Path

const val alertBaseUrl: String = "api/v1/alert"

interface AlertApi {

    @POST("$alertBaseUrl/create-alert/{recipientType}")
    suspend fun createAlert(@Path("recipientType") recipientType: BloodType): List<Alert>?

}
