package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankSettingsScreen(
    bloodBank: BloodBank? = null,
    bankName: String = "City Blood Bank",
    onEditProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    currentRoute: String = Screen.BloodBankSettings.route,
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get actual blood bank data from BloodBankProfileState
    val bloodBankData by BloodBankProfileState::savedBloodBankData
    
    // Use actual data or fallback to parameters/defaults
    val displayBankName = bloodBankData?.bloodBankName?.takeIf { it.isNotBlank() } ?: bankName
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        },
        bottomBar = {
            BloodBankBottomNavigation(
                currentRoute = Screen.BloodBankSettings.route,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(com.example.bloodlink.ui.theme.GrayLight)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Manage your blood bank settings and preferences",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Account Settings
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Edit Profile
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onEditProfileClick)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Edit Profile",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = com.example.bloodlink.ui.theme.Black
                                )
                                Text(
                                    text = "Update your blood bank information",
                                    fontSize = 14.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go to",
                            tint = GrayMedium
                        )
                    }
                    
                    Divider()
                    
                    // Change Password
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onChangePasswordClick)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Change Password",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = com.example.bloodlink.ui.theme.Black
                                )
                                Text(
                                    text = "Update your account password",
                                    fontSize = 14.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go to",
                            tint = GrayMedium
                        )
                    }
                }
            }

            // Blood Bank Information
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Blood Bank Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    ProfileField(label = "Name", value = displayBankName)
                    ProfileField(
                        label = "Email",
                        value = bloodBankData?.email?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                    ProfileField(
                        label = "Phone",
                        value = bloodBankData?.phone?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                    ProfileField(
                        label = "Address",
                        value = bloodBankData?.address?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                    ProfileField(
                        label = "License Number",
                        value = bloodBankData?.licenseNumber?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                }
            }

            // Logout
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BloodRed
                )
            ) {
                Text(
                    text = "Logout",
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = GrayMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = com.example.bloodlink.ui.theme.Black
        )
    }
}

