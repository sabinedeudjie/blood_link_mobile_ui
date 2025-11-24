package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.data.getRequestIdInt
import com.example.bloodlink.data.getRecipientType
import com.example.bloodlink.data.getQuantityNeeded
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.NotificationState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.screens.donor.DonorProfileState
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendAlertScreen(
    requestData: Map<String, Any>,
    bloodBankName: String,
    onBackClick: () -> Unit,
    onAlertSent: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }
    var alertSent by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Get eligible donors with matching blood type
    val recipientType = requestData.getRecipientType() ?: com.example.bloodlink.data.model.enums.BloodType.O_POSITIVE
    val eligibleDonorEmails = remember(recipientType) {
        UserDataStore.getEligibleDonorsByBloodType(recipientType)
            .filter { email ->
                // Check if donor is eligible (we need to check their eligibility status)
                // For now, we'll get all donors with matching blood type
                // In a real app, we'd check their eligibility status from a database
                true // We'll filter by eligibility when we have access to DonorProfileState
            }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        BloodLinkLogo(
                            size = 24.dp,
                            tint = White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Alert to Donors")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(com.example.bloodlink.ui.theme.GrayLight)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Request Information Card
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Request Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BloodTypeChip(bloodType = requestData.getRecipientType() ?: com.example.bloodlink.data.model.enums.BloodType.O_POSITIVE)
                        Text(
                            text = "${requestData.getQuantityNeeded() ?: 0} units",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Request ID: #${requestData.getRequestIdInt()}",
                        fontSize = 14.sp,
                        color = GrayMedium
                    )
                }
            }
            
            // Eligible Donors Count
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Eligible Donors",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                            Text(
                                text = "${eligibleDonorEmails.size} donors with ${recipientType.value} blood type",
                                fontSize = 14.sp,
                                color = GrayMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = BloodRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            if (alertSent) {
                // Success Message
                BloodLinkCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Alert Sent Successfully!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Text(
                                text = "Notifications have been sent to ${eligibleDonorEmails.size} eligible donors",
                                fontSize = 14.sp,
                                color = GrayMedium
                            )
                        }
                    }
                }
            } else {
                // Send Alert Button
                BloodLinkButton(
                    text = "Send Alert to All Eligible Donors",
                    onClick = {
                        if (eligibleDonorEmails.isNotEmpty()) {
                            isLoading = true
                            
                            // Send alert to all eligible donors
                            val currentBloodBankEmail = AuthState.currentUserEmail ?: ""
                            NotificationState.sendAlertToEligibleDonors(
                                donorEmails = eligibleDonorEmails,
                                bloodType = recipientType.value,
                                quantity = (requestData.getQuantityNeeded() ?: 0).toInt(),
                                requestId = requestData.getRequestIdInt(),
                                bloodBankName = bloodBankName,
                                bloodBankEmail = currentBloodBankEmail
                            )
                            
                            isLoading = false
                            alertSent = true
                            
                            // Call callback after a delay
                            coroutineScope.launch {
                                delay(2000)
                                onAlertSent()
                            }
                        }
                    },
                    enabled = !isLoading && eligibleDonorEmails.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            if (eligibleDonorEmails.isEmpty()) {
                BloodLinkCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOff,
                            contentDescription = null,
                            tint = GrayMedium,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Eligible Donors Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "There are no eligible donors with ${recipientType.value} blood type at the moment.",
                            fontSize = 14.sp,
                            color = GrayMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

