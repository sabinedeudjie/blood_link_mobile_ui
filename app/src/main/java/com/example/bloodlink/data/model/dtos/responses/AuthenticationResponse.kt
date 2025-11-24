package com.example.bloodlink.data.model.dtos.responses

import com.example.bloodlink.data.model.enums.UserRole

data class AuthenticationResponse (
    val token: String,
    val email: String,
    val role: UserRole
)
