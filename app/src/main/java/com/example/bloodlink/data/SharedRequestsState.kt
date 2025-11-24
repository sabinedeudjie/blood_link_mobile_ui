package com.example.bloodlink.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.data.model.enums.BloodType
import java.util.UUID

// Global shared state for blood requests - accessible by all blood banks
// Since BloodRequest is not a data class, we store data in Maps
object SharedRequestsState {
    private val allBloodRequestsData: MutableList<Map<String, Any>> = mutableStateListOf()
    var requestCount by mutableStateOf(0) // Track count to trigger recomposition
    
    // Add a new request (called when doctor creates a request)
    fun addBloodRequestData(requestData: Map<String, Any>) {
        allBloodRequestsData.add(requestData.toMutableMap())
        requestCount = allBloodRequestsData.size // Update count to trigger recomposition
    }
    
    // Get all pending requests as Maps
    fun getPendingRequestsData(): List<Map<String, Any>> {
        return allBloodRequestsData.filter { 
            (it["status"] as? RequestStatus) == RequestStatus.PENDING 
        }
    }
    
    // Get all requests as Maps
    fun getAllRequestsData(): List<Map<String, Any>> = allBloodRequestsData.toList()
    
    // Get requests for a specific blood bank (filtered by selectedBloodBankEmails)
    fun getRequestsForBloodBank(bloodBankEmail: String): List<Map<String, Any>> {
        val emailLower = bloodBankEmail.lowercase()
        return allBloodRequestsData.filter { requestData ->
            val selectedEmails = requestData["selectedBloodBankEmails"] as? List<*>
            selectedEmails?.any { (it as? String)?.lowercase() == emailLower } == true
        }
    }
    
    // For backward compatibility - return empty list of BloodRequest
    fun getAllRequests(): List<BloodRequest> = emptyList()
    
    // Update request status (when blood bank accepts/rejects)
    fun updateRequestStatus(requestIdInt: Int, status: RequestStatus) {
        val index = allBloodRequestsData.indexOfFirst { 
            (it["requestIdInt"] as? Int) == requestIdInt 
        }
        if (index != -1) {
            allBloodRequestsData[index] = allBloodRequestsData[index].toMutableMap().apply {
                put("status", status)
            }
            requestCount = allBloodRequestsData.size
        }
    }
}

// Extension functions to access blood request data from Maps
fun Map<String, Any>.getRequestId(): UUID? = this["id"] as? UUID
fun Map<String, Any>.getRequestIdInt(): Int = (this["requestIdInt"] as? Int) ?: (this["id"] as? UUID)?.hashCode() ?: 0
fun Map<String, Any>.getRecipientType(): BloodType? = this["recipientType"] as? BloodType
fun Map<String, Any>.getQuantityNeeded(): Long? = this["quantityNeeded"] as? Long
fun Map<String, Any>.getRequestStatus(): RequestStatus? = this["status"] as? RequestStatus
fun Map<String, Any>.getSelectedBloodBankEmails(): List<String> = (this["selectedBloodBankEmails"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

