package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodTypeChip
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White
import com.example.bloodlink.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffiliatedDonorsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentBloodBankEmail = AuthState.currentUserEmail ?: ""
    
    // Get all affiliated donors for this blood bank
    val affiliatedDonors = remember(currentBloodBankEmail) {
        UserDataStore.getAffiliatedDonors(currentBloodBankEmail)
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
                        Text("Affiliated Donors")
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
            // Summary Card
            BloodLinkCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Affiliated Donors",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                        Text(
                            text = "${affiliatedDonors.size}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = BloodRed
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = BloodRed.copy(alpha = 0.1f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
            
            Text(
                text = "Donors who have affiliated with your blood bank",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (affiliatedDonors.isEmpty()) {
                // Empty state
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
                            text = "No Affiliated Donors Yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Donors will appear here once they affiliate with your blood bank.",
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
                    items(affiliatedDonors) { (donorEmail, donorData) ->
                        AffiliatedDonorCard(
                            donorEmail = donorEmail,
                            donorData = donorData
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AffiliatedDonorCard(
    donorEmail: String,
    donorData: com.example.bloodlink.ui.screens.donor.PersonalDataForm
) {
    BloodLinkCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = BloodRed.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = donorEmail.take(2).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BloodRed
                    )
                }
            }
            
            // Donor Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = donorEmail,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.bloodlink.ui.theme.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Display medical profile information if available
                donorData?.let { data ->
                    if (data.birthdate != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = GrayMedium,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "DOB: ${java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data.birthdate)}",
                                fontSize = 12.sp,
                                color = GrayMedium
                            )
                        }
                    }
                    if (data.gender != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gender: ${data.gender.name}",
                            fontSize = 12.sp,
                            color = GrayMedium
                        )
                    }
                    if (data.weight > 0f) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Weight: ${data.weight} kg",
                            fontSize = 12.sp,
                            color = GrayMedium
                        )
                    }
                    if (data.emergencyContact.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContactEmergency,
                                contentDescription = null,
                                tint = GrayMedium,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Emergency: ${data.emergencyContact}",
                                fontSize = 12.sp,
                                color = GrayMedium
                            )
                        }
                    }
                }
            }
            
            // Status indicator
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SuccessGreen.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Affiliated",
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Affiliated",
                        fontSize = 12.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

