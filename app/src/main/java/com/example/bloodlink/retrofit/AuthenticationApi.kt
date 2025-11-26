package com.example.bloodlink.retrofit

import com.example.bloodlink.data.model.dtos.requests.LoginRequest
import com.example.bloodlink.data.model.dtos.requests.RegisterRequest
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import com.example.bloodlink.data.model.metiers.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
const val authBaseUrl: String = "api/v1/auth"
interface AuthenticationApi {

    @POST("$authBaseUrl/signUp")//signUp
    suspend fun signUp(@Body registerRequest: RegisterRequest): AuthenticationResponse?

    @POST("$authBaseUrl/logIn")//logIn
    suspend fun authenticate(@Body loginRequest: LoginRequest): AuthenticationResponse?

    @GET("$authBaseUrl/current-user")
    suspend fun getCurrentUser(): User
}
