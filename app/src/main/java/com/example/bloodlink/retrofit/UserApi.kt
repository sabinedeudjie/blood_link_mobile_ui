package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.metiers.User
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

const val userBaseUrl: String = "v1/user"

interface UserApi {

    @GET("$userBaseUrl/get_by_id/{id}")
    suspend fun getByID(@Path("id") id: UUID) : User?

    @POST("$userBaseUrl/getMe")
    suspend fun getMe(): User?




}
