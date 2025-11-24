package com.example.bloodlink.ui.screens.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.NotificationState
import com.example.bloodlink.data.DonorResponseStatus
import com.example.bloodlink.data.getNotificationId
import com.example.bloodlink.data.getMessage
import com.example.bloodlink.data.getTimestamp
import com.example.bloodlink.data.getIsRead
import com.example.bloodlink.data.getDonorResponseApproved
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.DonorBottomNavigation
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkOutlinedButton
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.ErrorRed
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorNotificationsScreen(
    notifications: List<Map<String, Any>> = emptyList(),
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Get current user email
    val currentEmail = AuthState.currentUserEmail
    
    // Get notifications from NotificationState for this donor
    // Observe notificationCount to trigger recomposition when new notifications are added
    val notificationCount by NotificationState::notificationCount
    val donorNotificationIds = remember(notificationCount, currentEmail) {
        if (currentEmail != null) {
            NotificationState.getNotificationIdsForDonor(currentEmail)
        } else {
            emptyList()
        }
    }
    
    // Get notification data for each ID
    val donorNotifications = remember(donorNotificationIds) {
        donorNotificationIds.mapNotNull { id ->
            NotificationState.getNotificationData(id)
        }
    }
    
    // Use notifications from state if available, otherwise use provided list
    // Sort by timestamp (newest first)
    val allNotifications = remember(donorNotifications, notifications) {
        val combined = if (donorNotifications.isNotEmpty()) donorNotifications else notifications
        combined.sortedByDescending { it.getTimestamp() }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BloodLinkLogo(
                            size = 24.dp,
                            tint = White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Blood Donation Alerts")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        },
        bottomBar = {
            DonorBottomNavigation(
                currentRoute = Screen.DonorNotifications.route,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(com.example.bloodlink.ui.theme.GrayLight)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Review and respond to blood donation requests",
                    fontSize = 14.sp,
                    color = GrayMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (allNotifications.isEmpty()) {
                // Variant 1: Empty state - No alerts
                item {
                    EmptyAlertsState()
                }
            } else {
                // Variant 2: List of alerts
                items(allNotifications) { notificationData ->
                    NotificationCard(
                        notification = notificationData,
                        onAccept = {
                            // Mark as accepted
                            if (currentEmail != null) {
                                val notificationId = notificationData.getNotificationId()
                                NotificationState.updateDonorResponse(
                                    currentEmail, 
                                    notificationId, 
                                    DonorResponseStatus.ACCEPTED
                                )
                                NotificationState.markAsRead(currentEmail, notificationId)
                            }
                        },
                        onDecline = {
                            // Mark as declined
                            if (currentEmail != null) {
                                val notificationId = notificationData.getNotificationId()
                                NotificationState.updateDonorResponse(
                                    currentEmail, 
                                    notificationId, 
                                    DonorResponseStatus.DECLINED
                                )
                                NotificationState.markAsRead(currentEmail, notificationId)
                            }
                        },
                        onClick = {
                            // Mark as read when clicked
                            if (currentEmail != null && !notificationData.getIsRead()) {
                                NotificationState.markAsRead(currentEmail, notificationData.getNotificationId())
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyAlertsState() {
    BloodLinkCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = GrayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Alerts Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Welcome to Blood Link! You'll receive notifications here when there are blood donation requests matching your blood type in your area. Make sure to complete your profile and enable notifications.",
                fontSize = 14.sp,
                color = GrayMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun NotificationCard(
    notification: Map<String, Any>,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    BloodLinkCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (!notification.getIsRead()) BloodRed else GrayMedium,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.getMessage(),
                        fontSize = 16.sp,
                        fontWeight = if (!notification.getIsRead()) FontWeight.Bold else FontWeight.Normal,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                            .format(notification.getTimestamp()),
                        fontSize = 12.sp,
                        color = GrayMedium
                    )
                    // Show response status if already responded
                    notification.getDonorResponseApproved()?.let { response ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (response == DonorResponseStatus.ACCEPTED) 
                                    Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (response == DonorResponseStatus.ACCEPTED) 
                                    SuccessGreen else ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (response == DonorResponseStatus.ACCEPTED) 
                                    "Accepted" else "Declined",
                                fontSize = 12.sp,
                                color = if (response == DonorResponseStatus.ACCEPTED) 
                                    SuccessGreen else ErrorRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                if (!notification.getIsRead()) {
                    Surface(
                        shape = CircleShape,
                        color = BloodRed,
                        modifier = Modifier.size(8.dp)
                    ) {}
                }
            }
            
            // Show action buttons if pending
            if (notification.getDonorResponseApproved() == null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BloodLinkButton(
                        text = "Accept",
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        enabled = true
                    )
                    BloodLinkOutlinedButton(
                        text = "Decline",
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                        enabled = true
                    )
                }
            }
        }
    }
}

