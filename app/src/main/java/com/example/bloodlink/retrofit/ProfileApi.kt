package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.dtos.requests.ProfileRequest
import com.example.bloodlink.data.model.dtos.requests.UpdateProfileRequest
import com.example.bloodlink.data.model.medicalProfile.MedicalProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

const val profileBaseUrl: String = "v1/medical-profile"

interface ProfileApi {

    @POST("$profileBaseUrl/create-profile")
    suspend fun createMedicalProfile(@Body profileRequest: ProfileRequest): MedicalProfile?

    @GET("$profileBaseUrl/get-profile/id={donorId}")
    suspend fun getProfile(@Path("donorId") donorId: UUID): MedicalProfile?

    @PUT("$profileBaseUrl/update-profile")
    suspend fun updateProfile(@Body updateRequest: UpdateProfileRequest): MedicalProfile?

}
