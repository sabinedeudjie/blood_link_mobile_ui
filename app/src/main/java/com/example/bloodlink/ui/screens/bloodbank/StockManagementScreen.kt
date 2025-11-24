package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.appl.StockByType
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockManagementScreen(
    stock: List<StockByType> = emptyList(),
    onStockItemClick: (Int) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock Management") },
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
                currentRoute = Screen.StockManagement.route,
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
            if (stock.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "No Stock",
                            tint = GrayMedium,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Stock Data",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Text(
                            text = "Blood stock information will appear here",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                }
            } else {
                items(stock) { stockItem ->
                    BloodLinkCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BloodTypeChip(bloodType = stockItem.bloodType ?: com.example.bloodlink.data.model.enums.BloodType.O_POSITIVE)
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${stockItem.quantity ?: 0} units",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.bloodlink.ui.theme.Black
                                )
                                Text(
                                    text = "Available",
                                    fontSize = 12.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



