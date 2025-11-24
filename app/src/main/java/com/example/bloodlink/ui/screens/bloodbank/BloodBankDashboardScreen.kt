package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.bloodlink.data.model.appl.StockByType
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.StatusBadge
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodRed
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankDashboardScreen(
    bankName: String = "",
    onProfileClick: () -> Unit,
    onManageRequestsClick: () -> Unit,
    onStockManagementClick: () -> Unit,
    onCreateNotificationClick: () -> Unit,
    onAlertResponsesClick: () -> Unit = {},
    onAffiliatedDonorsClick: () -> Unit = {},
    onCreateCustomAlertClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    currentRoute: String = Screen.BloodBankDashboard.route,
    bloodRequests: List<BloodRequest> = emptyList(),
    stock: List<StockByType> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Get actual blood bank data from BloodBankProfileState
    val bloodBankData by BloodBankProfileState::savedBloodBankData
    
    // Use actual data or fallback to parameters/defaults
    val displayBankName = bloodBankData?.bloodBankName?.takeIf { it.isNotBlank() } ?: bankName
    
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
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Blood Link",
                            tint = com.example.bloodlink.ui.theme.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BloodLink")
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
                currentRoute = currentRoute,
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
                WelcomeCard(bankName = displayBankName)
            }

            item {
                QuickActionsCard(
                    onManageRequestsClick = onManageRequestsClick,
                    onStockManagementClick = onStockManagementClick,
                    onCreateNotificationClick = onCreateNotificationClick,
                    onAffiliatedDonorsClick = onAffiliatedDonorsClick,
                    onCreateCustomAlertClick = onCreateCustomAlertClick
                )
            }

            item {
                Text(
                    text = "Blood Stock",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (stock.isEmpty()) {
                item {
                    Text(
                        text = "No stock data available",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.GrayMedium
                    )
                }
            } else {
                items(stock) { stockItem ->
                    StockCard(stock = stockItem)
                }
            }

            item {
                Text(
                    text = "Pending Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (allRequestsData.isEmpty()) {
                item {
                    Text(
                        text = "No pending requests",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.GrayMedium
                    )
                }
            } else {
                items(allRequestsData.take(5)) { requestData ->
                    BloodRequestCardFromMap(
                        requestData = requestData,
                        onClick = { onManageRequestsClick() }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(bankName: String) {
    BloodLinkCard {
        Column {
            Text(
                text = "Welcome",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bankName,
                fontSize = 16.sp,
                color = com.example.bloodlink.ui.theme.GrayMedium
            )
        }
    }
}

@Composable
fun QuickActionsCard(
    onManageRequestsClick: () -> Unit,
    onStockManagementClick: () -> Unit,
    onCreateNotificationClick: () -> Unit,
    onAlertResponsesClick: () -> Unit = {},
    onAffiliatedDonorsClick: () -> Unit = {},
    onCreateCustomAlertClick: () -> Unit = {}
) {
    BloodLinkCard {
        Column {
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "Requests",
                    icon = Icons.Default.List,
                    onClick = onManageRequestsClick,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Stock",
                    icon = Icons.Default.Inventory,
                    onClick = onStockManagementClick,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "Custom Alert",
                    icon = Icons.Default.Send,
                    onClick = onCreateCustomAlertClick,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Donors",
                    icon = Icons.Default.Person,
                    onClick = onAffiliatedDonorsClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = BloodRed
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = text, fontSize = 12.sp)
        }
    }
}

@Composable
fun StockCard(stock: StockByType) {
    BloodLinkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BloodTypeChip(bloodType = stock.bloodType ?: com.example.bloodlink.data.model.enums.BloodType.O_POSITIVE)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${stock.quantity ?: 0} units",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if ((stock.quantity ?: 0) < 10) "Low Stock" else "In Stock",
                    fontSize = 12.sp,
                    color = if ((stock.quantity ?: 0) < 10) com.example.bloodlink.ui.theme.ErrorRed else com.example.bloodlink.ui.theme.SuccessGreen
                )
            }
        }
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

