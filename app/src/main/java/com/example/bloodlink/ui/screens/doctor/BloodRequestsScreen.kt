package com.example.bloodlink.ui.screens.doctor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.bloodlink.data.getRequestStatus
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.DoctorBottomNavigation
import com.example.bloodlink.ui.components.StatusBadge
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodRequestsScreen(
    bloodRequests: List<BloodRequest> = emptyList(),
    onRequestClick: (Int) -> Unit = {},
    onCreateRequestClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Observe saved requests from DoctorProfileState
    val requestCount by DoctorProfileState::requestCount // Observe count changes
    
    // Use remember with requestCount as key to trigger recomposition when requests are added
    // Get requests from DoctorProfileState as Maps
    val allRequestsData = remember(requestCount) {
        DoctorProfileState.getAllRequestsData()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blood Requests") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = com.example.bloodlink.ui.theme.White
                ),
                actions = {
                    IconButton(onClick = onCreateRequestClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Request",
                            tint = com.example.bloodlink.ui.theme.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRequestClick,
                containerColor = BloodRed
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Request",
                    tint = com.example.bloodlink.ui.theme.White
                )
            }
        },
        bottomBar = {
            DoctorBottomNavigation(
                currentRoute = Screen.BloodRequests.route,
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
                            text = "No Blood Requests",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Text(
                            text = "Your blood requests will appear here",
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
                        }
                    }
                }
            }
        }
    }
}

