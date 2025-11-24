package com.example.bloodlink.ui.screens.doctor

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
import com.example.bloodlink.data.model.metiers.Doctor
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.DoctorBottomNavigation
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    doctor: Doctor? = null,
    userName: String = "Dr. Smith",
    hospitalName: String = "City Hospital",
    onEditProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get actual doctor data from DoctorProfileState
    val doctorData by DoctorProfileState::savedDoctorData
    
    // Use actual data or fallback to parameters/defaults
    val doctorName = doctorData?.let { "Dr. ${it.firstName} ${it.lastName}".trim() }
        .takeIf { !it.isNullOrBlank() } ?: userName
    val doctorHospital = doctorData?.hospitalName?.takeIf { it.isNotBlank() } ?: hospitalName
    val doctorSpecialty = doctorData?.specialization?.takeIf { it.isNotBlank() } 
        ?: (doctor?.speciality ?: "General Medicine")
    val doctorEmail = doctorData?.email?.takeIf { it.isNotBlank() } ?: "doctor@example.com"
    val doctorPhone = doctorData?.phone?.takeIf { it.isNotBlank() } ?: "+1234567890"
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
                        Text("Doctor Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        },
        bottomBar = {
            DoctorBottomNavigation(
                currentRoute = Screen.DoctorProfile.route,
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
                text = "Manage your profile information",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Personal Information Card
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = BloodRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Personal Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileField(label = "Name", value = doctorName)
                    ProfileField(label = "Hospital", value = doctorHospital)
                    ProfileField(label = "Specialty", value = doctorSpecialty)
                    ProfileField(label = "Email", value = doctorEmail)
                    ProfileField(label = "Phone", value = doctorPhone)
                    ProfileField(
                        label = "Medical License Number", 
                        value = doctorData?.medicalLicenseNumber?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                }
            }

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
                                    text = "Update your doctor information",
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

