package com.example.bloodlink.data.model.dtos.requests

import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.UserRole
import java.util.UUID


data class RegisterRequest (

    // User information
    val name: String?,
    val surname: String?,
    val email: String?,
    val password: String?,
    val phoneNumber: String?,
    val userRole: UserRole?,

    // Donor's information
    val LastDonationDate: String? = null,
    val bloodType: BloodType? = null,

    //Doctor's information
    val orderLicense: UUID? = null,
    val speciality: String? = null,
    val hospitalName: String? = null,

    //BloodBank's information
    val bloodBankName: String? = null,
    val address: String? = null,
    val licenseNumber: UUID? = null

)
