package com.example.bloodlink.ui.screens.doctor

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bloodlink.data.model.appl.BloodRequest

// Data class for doctor form data
data class DoctorDataForm(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val hospitalName: String = "",
    val specialization: String = "",
    val medicalLicenseNumber: String = ""
)

// Shared state to persist data across navigation
object DoctorProfileState {
    var savedDoctorData: DoctorDataForm? by mutableStateOf(null)
    private val bloodRequestsData: MutableList<Map<String, Any>> = mutableStateListOf()
    var requestCount by mutableStateOf(0) // Track count to trigger recomposition
    var password: String by mutableStateOf("") // Store password for change password functionality
    
    // For backward compatibility - return empty list
    val bloodRequests: MutableList<BloodRequest> = mutableStateListOf()
    
    // Helper function to add a request data (Map) and trigger recomposition
    fun addBloodRequestData(requestData: Map<String, Any>) {
        bloodRequestsData.add(requestData.toMutableMap())
        requestCount = bloodRequestsData.size // Update count to trigger recomposition
    }
    
    // Helper function to get all requests data as a list (for observation)
    fun getAllRequestsData(): List<Map<String, Any>> = bloodRequestsData.toList()
    
    // Helper function to get all requests as a list (for backward compatibility)
    fun getAllRequests(): List<BloodRequest> = emptyList()
}

