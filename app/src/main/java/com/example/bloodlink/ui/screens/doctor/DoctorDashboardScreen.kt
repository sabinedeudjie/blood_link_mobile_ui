package com.example.bloodlink.ui.screens.doctor

import androidx.compose.foundation.clickable
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
import com.example.bloodlink.data.getRequestStatus
import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.StatusBadge
import com.example.bloodlink.ui.components.DoctorBottomNavigation
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodRed
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDashboardScreen(
    userName: String = "Dr. Smith",
    hospitalName: String = "City Hospital",
    onProfileClick: () -> Unit,
    onCreateRequestClick: () -> Unit,
    onRequestClick: (Int) -> Unit,
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    bloodRequests: List<BloodRequest> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Get actual doctor data from DoctorProfileState
    val doctorData by DoctorProfileState::savedDoctorData
    val requestCount by DoctorProfileState::requestCount // Observe count changes
    
    // Use actual data or fallback to parameters/defaults
    val doctorName = doctorData?.let { "Dr. ${it.firstName} ${it.lastName}".trim() }
        .takeIf { !it.isNullOrBlank() } ?: userName
    val doctorHospital = doctorData?.hospitalName?.takeIf { it.isNotBlank() } ?: hospitalName
    
    // Observe blood requests list - use remember with requestCount as key to trigger recomposition
    // Get requests from DoctorProfileState as Maps
    val allRequestsData = remember(requestCount) {
        DoctorProfileState.getAllRequestsData()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BloodLinkLogo(
                            size = 24.dp,
                            tint = com.example.bloodlink.ui.theme.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BloodLink - Doctor")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = com.example.bloodlink.ui.theme.White
                )
            )
        },
        bottomBar = {
            DoctorBottomNavigation(
                currentRoute = Screen.DoctorDashboard.route,
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
            item {
                WelcomeCard(userName = doctorName, hospitalName = doctorHospital)
            }

            item {
                StatsCard(bloodRequestsData = allRequestsData)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Blood Requests",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    // Always show create button, even when there are requests
                    Button(
                        onClick = onCreateRequestClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BloodRed
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "New Request",
                            color = com.example.bloodlink.ui.theme.White
                        )
                    }
                }
            }

            if (allRequestsData.isEmpty()) {
                item {
                    EmptyStateCard(
                        message = "No blood requests yet",
                        actionText = "Create Your First Request",
                        onActionClick = onCreateRequestClick
                    )
                }
            } else {
                items(allRequestsData) { requestData ->
                    BloodRequestCardFromMap(
                        requestData = requestData,
                        onClick = { onRequestClick(requestData.getRequestIdInt()) }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(userName: String, hospitalName: String) {
    BloodLinkCard {
        Column {
            Text(
                text = "Welcome, $userName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hospitalName,
                fontSize = 16.sp,
                color = com.example.bloodlink.ui.theme.GrayMedium
            )
        }
    }
}

@Composable
fun StatsCard(bloodRequestsData: List<Map<String, Any>>) {
    BloodLinkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                label = "Total",
                value = bloodRequestsData.size.toString(),
                icon = Icons.Default.List
            )
            StatItem(
                label = "Pending",
                value = bloodRequestsData.count { it.getRequestStatus() == RequestStatus.PENDING }.toString(),
                icon = Icons.Default.Schedule
            )
            StatItem(
                label = "Accepted",
                value = bloodRequestsData.count { it.getRequestStatus() == RequestStatus.ACCEPTED }.toString(),
                icon = Icons.Default.CheckCircle
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = BloodRed,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = com.example.bloodlink.ui.theme.GrayMedium
        )
    }
}

@Composable
fun BloodRequestCardFromMap(
    requestData: Map<String, Any>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                fontSize = 16.sp
            )
            Text(
                text = "Request ID: #${requestData.getRequestIdInt()}",
                fontSize = 14.sp,
                color = com.example.bloodlink.ui.theme.GrayMedium
            )
        }
    }
}

@Composable
fun EmptyStateCard(
    message: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    BloodLinkCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = com.example.bloodlink.ui.theme.GrayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = com.example.bloodlink.ui.theme.GrayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BloodRed
                )
            ) {
                Text(
                    text = actionText,
                    color = com.example.bloodlink.ui.theme.White
                )
            }
        }
    }
}

