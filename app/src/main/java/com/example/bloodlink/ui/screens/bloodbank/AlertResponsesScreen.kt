package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.bloodlink.data.NotificationState
import com.example.bloodlink.data.getNotificationRequestId
import com.example.bloodlink.data.DonorResponseStatus
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.data.getNotificationId
import com.example.bloodlink.data.getMessage
import com.example.bloodlink.data.getTimestamp
import com.example.bloodlink.data.getRequestId
import com.example.bloodlink.data.getDonorEmail
import com.example.bloodlink.data.getDonorResponseApproved
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.screens.donor.PersonalDataForm
import com.example.bloodlink.ui.screens.donor.VitalSignsForm
import com.example.bloodlink.ui.screens.donor.checkDonorEligibility
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.ErrorRed
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertResponsesScreen(
    requestId: Int? = null,
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    showBottomNavigation: Boolean = false,
    modifier: Modifier = Modifier
) {
    val currentBloodBankEmail = AuthState.currentUserEmail ?: ""
    
    // Get all notification IDs for this blood bank
    val allResponseIds = remember(requestId, currentBloodBankEmail) {
        if (requestId != null) {
            NotificationState.getResponseIdsForRequest(requestId, currentBloodBankEmail)
        } else {
            NotificationState.getNotificationIdsForBloodBank(currentBloodBankEmail)
                .filter { notificationId ->
                    val data = NotificationState.getNotificationData(notificationId)
                    data?.getDonorResponseApproved() != null // Only responses (not pending)
                }
        }
    }
    
    var selectedDonorEmail by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!showBottomNavigation) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        BloodLinkLogo(
                            size = 24.dp,
                            tint = White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (showBottomNavigation) "Notifications" else "Donor Responses")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        },
        bottomBar = {
            if (showBottomNavigation) {
                BloodBankBottomNavigation(
                    currentRoute = Screen.AlertResponses.route,
                    onNavigate = onNavigate
                )
            }
        }
    ) { paddingValues ->
        if (selectedDonorEmail != null) {
            // Show donor profile and eligibility check
            DonorEligibilityCheckScreen(
                donorEmail = selectedDonorEmail!!,
                notificationId = allResponseIds.firstOrNull { notificationId ->
                    NotificationState.getNotificationData(notificationId)?.getDonorEmail() == selectedDonorEmail
                },
                onBackClick = { selectedDonorEmail = null },
                modifier = modifier
            )
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(com.example.bloodlink.ui.theme.GrayLight)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Donors who responded to your alerts",
                    fontSize = 14.sp,
                    color = GrayMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (allResponseIds.isEmpty()) {
                    BloodLinkCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonOff,
                                contentDescription = null,
                                tint = GrayMedium,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No Responses Yet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Donors will appear here when they respond to your alerts.",
                                fontSize = 14.sp,
                                color = GrayMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(allResponseIds) { notificationId ->
                            val notificationData = NotificationState.getNotificationData(notificationId)
                            if (notificationData != null) {
                                ResponseCard(
                                    notificationData = notificationData,
                                    onClick = {
                                        selectedDonorEmail = notificationData.getDonorEmail()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResponseCard(
    notificationData: Map<String, Any>,
    onClick: () -> Unit
) {
    val responseStatus = notificationData.getDonorResponseApproved()
    BloodLinkCard(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (responseStatus) {
                    DonorResponseStatus.ACCEPTED -> SuccessGreen.copy(alpha = 0.1f)
                    DonorResponseStatus.DECLINED -> ErrorRed.copy(alpha = 0.1f)
                    else -> GrayMedium.copy(alpha = 0.1f)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = when (responseStatus) {
                            DonorResponseStatus.ACCEPTED -> Icons.Default.CheckCircle
                            DonorResponseStatus.DECLINED -> Icons.Default.Cancel
                            else -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = when (responseStatus) {
                            DonorResponseStatus.ACCEPTED -> SuccessGreen
                            DonorResponseStatus.DECLINED -> ErrorRed
                            else -> GrayMedium
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Request #${notificationData.getNotificationRequestId()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.bloodlink.ui.theme.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificationData.getMessage(),
                    fontSize = 14.sp,
                    color = GrayMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                        .format(notificationData.getTimestamp()),
                    fontSize = 12.sp,
                    color = GrayMedium
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View Details",
                tint = BloodRed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorEligibilityCheckScreen(
    donorEmail: String,
    notificationId: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get donor data
    val donorData = remember(donorEmail) {
        UserDataStore.getDonorData(donorEmail)
    }
    val donorVitalSigns = remember(donorEmail) {
        UserDataStore.getDonorVitalSigns(donorEmail)
    }
    val donorHealthQuestions = remember(donorEmail) {
        UserDataStore.getDonorHealthQuestions(donorEmail)
    }
    
    var eligibilityResult by remember { mutableStateOf<Boolean?>(null) }
    
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
                        Text("Check Donor Eligibility")
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
            // Donor Information
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Donor Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    donorData?.let { data ->
                        Text("Email: $donorEmail", fontSize = 14.sp)
                        if (data.birthdate != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Date of Birth: ${java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data.birthdate)}", fontSize = 14.sp)
                        }
                        if (data.gender != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Gender: ${data.gender.name}", fontSize = 14.sp)
                        }
                        if (data.weight > 0f) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Weight: ${data.weight} kg", fontSize = 14.sp)
                        }
                        if (data.emergencyContact.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Emergency Contact: ${data.emergencyContact}", fontSize = 14.sp)
                        }
                    } ?: Text("Donor data not found", fontSize = 14.sp, color = ErrorRed)
                }
            }
            
            // Health Profile Summary
            if (donorVitalSigns != null && donorHealthQuestions != null) {
                BloodLinkCard {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Health Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        if (donorVitalSigns.hemoglobinLevel > 0f) {
                            Text("Hemoglobin Level: ${donorVitalSigns.hemoglobinLevel} g/dL", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        if (donorVitalSigns.bodyTemperature > 0f) {
                            Text("Body Temperature: ${donorVitalSigns.bodyTemperature} °C", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        if (donorVitalSigns.pulseRate > 0f) {
                            Text("Pulse Rate: ${donorVitalSigns.pulseRate} bpm", fontSize = 14.sp)
                        }
                    }
                }
            } else {
                BloodLinkCard {
                    Text(
                        text = "Health profile incomplete",
                        fontSize = 14.sp,
                        color = ErrorRed
                    )
                }
            }
            
            // Eligibility Check Button
            BloodLinkButton(
                text = "Check Eligibility",
                onClick = {
                    eligibilityResult = checkDonorEligibility(
                        personalData = donorData,
                        vitalSigns = donorVitalSigns,
                        healthQuestions = donorHealthQuestions,
                        gender = donorData?.gender
                    )
                    
                    // Save eligibility result
                    if (eligibilityResult != null) {
                        UserDataStore.setDonorEligibility(donorEmail, eligibilityResult!!)
                    }
                },
                enabled = donorData != null && donorVitalSigns != null && donorHealthQuestions != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Eligibility Result
            eligibilityResult?.let { eligible ->
                BloodLinkCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (eligible) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (eligible) SuccessGreen else ErrorRed,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (eligible) "Eligible to Donate" else "Not Eligible to Donate",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (eligible) SuccessGreen else ErrorRed
                            )
                            Text(
                                text = if (eligible) 
                                    "This donor meets all requirements for blood donation" 
                                else 
                                    "This donor does not meet the requirements for blood donation",
                                fontSize = 14.sp,
                                color = GrayMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

