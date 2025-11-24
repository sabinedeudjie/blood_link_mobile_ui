package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.dtos.requests.LoginRequest
import com.example.bloodlink.data.model.dtos.requests.RegisterRequest
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST
const val authBaseUrl: String = "api/v1/auth"
interface AuthenticationApi {

    @POST("$authBaseUrl/signUp")
    suspend fun signUp(@Body registerRequest: RegisterRequest): AuthenticationResponse?

    @POST("$authBaseUrl/logIn")
    suspend fun authenticate(@Body loginRequest: LoginRequest): AuthenticationResponse?

}
