package com.example.bloodlink.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bloodlink.data.model.appl.Notification
import com.example.bloodlink.data.model.appl.DonorResponse
import com.example.bloodlink.data.model.enums.AlertStatus
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

// Global state to manage notifications for all donors
// Uses a Map-based storage system since Notification class can't be easily instantiated
object NotificationState {
    // Store notification UI data in Maps (notificationId -> data map)
    val notificationData = mutableMapOf<String, MutableMap<String, Any>>()
    // Store notifications by donor email (email -> list of notification IDs)
    val notificationsByEmail = mutableMapOf<String, MutableList<String>>()
    var notificationCount by mutableStateOf(0) // Track count to trigger recomposition
    
    // Add a notification for a specific donor
    fun addNotification(donorEmail: String, notificationId: String, data: MutableMap<String, Any>) {
        val emailLower = donorEmail.lowercase()
        if (!notificationsByEmail.containsKey(emailLower)) {
            notificationsByEmail[emailLower] = mutableStateListOf()
        }
        notificationData[notificationId] = data
        notificationsByEmail[emailLower]?.add(0, notificationId)
        notificationCount = notificationsByEmail.values.sumOf { it.size }
    }
    
    // Get all notifications for a specific donor
    // Returns empty list since we can't easily create Notification instances
    // UI code should use getNotificationData() instead
    fun getNotificationsForDonor(donorEmail: String): List<Notification> {
        // Return empty list - UI should use getNotificationData() and extensions
        return emptyList()
    }
    
    // Get notification data by ID
    fun getNotificationData(notificationId: String): Map<String, Any>? {
        return notificationData[notificationId]
    }
    
    // Get all notification IDs for a donor
    fun getNotificationIdsForDonor(donorEmail: String): List<String> {
        return notificationsByEmail[donorEmail.lowercase()]?.toList() ?: emptyList()
    }
    
    // Mark a notification as read
    fun markAsRead(donorEmail: String, notificationId: String) {
        notificationData[notificationId]?.let { data ->
            data["isRead"] = true
            notificationCount = notificationsByEmail.values.sumOf { it.size }
        }
    }
    
    // Send alert to multiple eligible donors
    fun sendAlertToEligibleDonors(
        donorEmails: List<String>,
        bloodType: String,
        quantity: Int,
        requestId: Int,
        bloodBankName: String,
        bloodBankEmail: String
    ) {
        val message = "Urgent: $bloodType blood needed! $bloodBankName is requesting $quantity units. Please consider donating."
        val baseNotificationId = "req_${requestId}_${Date().time}"
        
        donorEmails.forEach { email ->
            val notificationId = "${baseNotificationId}_${email}"
            val data = mutableMapOf<String, Any>(
                "notificationId" to notificationId,
                "message" to message,
                "timestamp" to Date(),
                "isRead" to false,
                "requestId" to requestId,
                "bloodBankEmail" to bloodBankEmail,
                "donorEmail" to email
            )
            // Use a special string value to represent null/pending
            data["donorResponseApproved"] = "PENDING" // "PENDING" = pending, true = accepted, false = declined
            addNotification(email, notificationId, data)
        }
    }
    
    // Update donor response to an alert
    fun updateDonorResponse(donorEmail: String, notificationId: String, isApproved: Boolean) {
        notificationData[notificationId]?.let { data ->
            data["donorResponseApproved"] = isApproved as Any
            notificationCount = notificationsByEmail.values.sumOf { it.size }
        }
    }
    
    // Get all notification IDs for a blood bank
    fun getNotificationIdsForBloodBank(bloodBankEmail: String): List<String> {
        val emailLower = bloodBankEmail.lowercase()
        return notificationData.entries
            .filter { (_, data) -> (data["bloodBankEmail"] as? String)?.lowercase() == emailLower }
            .map { it.key }
    }
    
    // Get all responses for a specific request
    fun getResponseIdsForRequest(requestId: Int, bloodBankEmail: String): List<String> {
        val emailLower = bloodBankEmail.lowercase()
        return notificationData.entries
            .filter { (_, data) ->
                (data["requestId"] as? Int) == requestId &&
                (data["bloodBankEmail"] as? String)?.lowercase() == emailLower &&
                data["donorResponseApproved"] != "PENDING"
            }
            .map { it.key }
    }
    
    // Send custom alert to donors (without a blood request)
    fun sendCustomAlert(
        donorEmails: List<String>,
        message: String,
        bloodBankName: String,
        bloodBankEmail: String,
        bloodType: String? = null,
        quantity: Int? = null
    ) {
        val baseNotificationId = "custom_${Date().time}"
        
        donorEmails.forEach { email ->
            val notificationId = "${baseNotificationId}_${email}"
            val data = mutableMapOf<String, Any>(
                "notificationId" to notificationId,
                "message" to message,
                "timestamp" to Date(),
                "isRead" to false,
                "requestId" to 0,
                "bloodBankEmail" to bloodBankEmail,
                "donorEmail" to email
            )
            // Use a special string value to represent null/pending
            data["donorResponseApproved"] = "PENDING" // "PENDING" = pending, true = accepted, false = declined
            addNotification(email, notificationId, data)
        }
    }
}

// Extension properties to access notification data from Maps
// These provide a bridge between the Map storage and UI code
fun Map<String, Any>.getNotificationId(): String = this["notificationId"] as? String ?: ""
fun Map<String, Any>.getMessage(): String = this["message"] as? String ?: ""
fun Map<String, Any>.getTimestamp(): Date = this["timestamp"] as? Date ?: Date()
fun Map<String, Any>.getIsRead(): Boolean = this["isRead"] as? Boolean ?: false
fun Map<String, Any>.getNotificationRequestId(): Int = this["requestId"] as? Int ?: 0
fun Map<String, Any>.getBloodBankEmail(): String = this["bloodBankEmail"] as? String ?: ""
fun Map<String, Any>.getDonorEmail(): String = this["donorEmail"] as? String ?: ""
fun Map<String, Any>.getDonorResponseApproved(): Boolean? = this["donorResponseApproved"] as? Boolean?

// Helper object for DonorResponse status comparison
object DonorResponseStatus {
    val PENDING: Boolean? = null
    val ACCEPTED = true
    val DECLINED = false
}
