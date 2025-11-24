package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Data class for blood bank form data
data class BloodBankDataForm(
    val bloodBankName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val licenseNumber: String = ""
)

// Shared state to persist data across navigation
object BloodBankProfileState {
    var savedBloodBankData: BloodBankDataForm? by mutableStateOf(null)
    var password: String by mutableStateOf("") // Store password for change password functionality
}

