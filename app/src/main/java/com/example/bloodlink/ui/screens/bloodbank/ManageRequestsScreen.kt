package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.bloodlink.data.SharedRequestsState
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.getRequestIdInt
import com.example.bloodlink.data.getRecipientType
import com.example.bloodlink.data.getQuantityNeeded
import com.example.bloodlink.data.getRequestStatus
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.appl.BloodRequest
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.ui.components.StatusBadge
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageRequestsScreen(
    bloodRequests: List<BloodRequest> = emptyList(),
    onRequestClick: (Int) -> Unit = {},
    onSendAlertClick: (BloodRequest) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get current blood bank email
    val currentBloodBankEmail = AuthState.currentUserEmail ?: ""
    
    // Get shared requests from global state (requests from all doctors)
    // Filter by blood bank email to show only requests for this blood bank
    val sharedRequestCount by SharedRequestsState::requestCount
    val allRequestsData = remember(sharedRequestCount, currentBloodBankEmail) {
        if (currentBloodBankEmail.isNotBlank()) {
            SharedRequestsState.getRequestsForBloodBank(currentBloodBankEmail)
        } else {
            SharedRequestsState.getAllRequestsData()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Requests") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.bloodlink.ui.theme.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = com.example.bloodlink.ui.theme.White
                )
            )
        },
        bottomBar = {
            BloodBankBottomNavigation(
                currentRoute = Screen.ManageRequests.route,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (allRequestsData.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = "No Requests",
                            tint = GrayMedium,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Requests",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Text(
                            text = "Blood requests will appear here",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                }
            } else {
                items(allRequestsData) { requestData ->
                    BloodLinkCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BloodTypeChip(bloodType = requestData.getRecipientType() ?: com.example.bloodlink.data.model.enums.BloodType.O_POSITIVE)
                                StatusBadge(status = requestData.getRequestStatus() ?: com.example.bloodlink.data.model.enums.RequestStatus.PENDING)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Quantity: ${requestData.getQuantityNeeded() ?: 0} units",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Request ID: #${requestData.getRequestIdInt()}",
                                fontSize = 14.sp,
                                color = GrayMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            BloodLinkButton(
                                text = "Send Alert to Eligible Donors",
                                onClick = { onRequestClick(requestData.getRequestIdInt()) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

