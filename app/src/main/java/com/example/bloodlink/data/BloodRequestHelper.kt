package com.example.bloodlink.data

import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.RequestStatus
import java.time.LocalDateTime
import java.util.UUID

// Helper object to work with BloodRequest since it's not a data class
object BloodRequestHelper {
    // Store request data in Maps (requestId -> data map)
    private val requestData = mutableMapOf<UUID, MutableMap<String, Any>>()
    
    // Create a BloodRequest instance (will have null values, but we store real data in Maps)
    fun createRequest(
        id: UUID = UUID.randomUUID(),
        recipientType: BloodType,
        quantityNeeded: Long,
        status: RequestStatus = RequestStatus.PENDING,
        selectedBloodBankEmails: List<String> = emptyList()
    ): BloodRequest {
        val request = BloodRequest()
        // Store the actual data in our Map
        requestData[id] = mutableMapOf(
            "id" to id,
            "recipientType" to recipientType,
            "quantityNeeded" to quantityNeeded,
            "status" to status,
            "selectedBloodBankEmails" to selectedBloodBankEmails,
            "createdAt" to LocalDateTime.now()
        )
        return request
    }
    
    // Get request data
    fun getRequestData(request: BloodRequest): Map<String, Any>? {
        return request.id?.let { requestData[it] }
    }
    
    // Helper extensions to access BloodRequest properties
    fun BloodRequest.getRecipientType(): BloodType? {
        return this.id?.let { requestData[it]?.get("recipientType") as? BloodType } ?: this.recipientType
    }
    
    fun BloodRequest.getQuantityNeeded(): Long? {
        return this.id?.let { requestData[it]?.get("quantityNeeded") as? Long } ?: this.quantityNeeded
    }
    
    fun BloodRequest.getStatus(): RequestStatus? {
        return this.id?.let { requestData[it]?.get("status") as? RequestStatus } ?: this.status
    }
    
    fun BloodRequest.getSelectedBloodBankEmails(): List<String> {
        return this.id?.let { requestData[it]?.get("selectedBloodBankEmails") as? List<String> } ?: emptyList()
    }
    
    // For UI compatibility - map old property names to new ones
    val BloodRequest.bloodType: BloodType?
        get() = getRecipientType()
    
    val BloodRequest.quantity: Int
        get() = (getQuantityNeeded() ?: 0L).toInt()
    
    val BloodRequest.requestStatus: RequestStatus?
        get() = getStatus()
    
    val BloodRequest.bloodRequestId: Int
        get() = id?.hashCode() ?: 0
}

