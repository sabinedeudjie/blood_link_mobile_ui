package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodBankBottomNavigation
import com.example.bloodlink.ui.screens.doctor.ProfileField // Reusing for consistency
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBankProfileScreen(
    bloodBank: BloodBank? = null,
    bankName: String = "",
    onEditProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get actual blood bank data from BloodBankProfileState
    val bloodBankData by BloodBankProfileState::savedBloodBankData
    
    // Use actual data or fallback to parameters/defaults
    val displayBankName = bloodBankData?.bloodBankName?.takeIf { it.isNotBlank() } ?: bankName
    val bankAddress = bloodBankData?.address?.takeIf { it.isNotBlank() } 
        ?: (bloodBank?.address ?: "Not provided")
    val bankEmail = bloodBankData?.email?.takeIf { it.isNotBlank() } ?: "bloodbank@example.com"
    val bankPhone = bloodBankData?.phone?.takeIf { it.isNotBlank() } ?: "+1234567890"
    val bankLicense = bloodBankData?.licenseNumber?.takeIf { it.isNotBlank() } ?: "Not provided"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Blood Link",
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Blood Bank Profile")
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
                currentRoute = Screen.BloodBankProfile.route,
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
                text = "Manage your blood bank profile information",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Blood Bank Information Card
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Blood Bank Information",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                        }
                        IconButton(onClick = onEditProfileClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = BloodRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileField(label = "Blood Bank Name", value = displayBankName)
                    ProfileField(label = "Address", value = bankAddress)
                    ProfileField(label = "Email", value = bankEmail)
                    ProfileField(label = "Phone", value = bankPhone)
                    ProfileField(label = "License Number", value = bankLicense)
                }
            }

            // Change Password Card
            BloodLinkCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onChangePasswordClick)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Change Password",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Change Password",
                            tint = BloodRed
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Update your password to keep your account secure",
                        fontSize = 14.sp,
                        color = GrayMedium
                    )
                }
            }

            // Logout Button
            Spacer(modifier = Modifier.height(8.dp))
            BloodLinkButton(
                text = "Logout",
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
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

