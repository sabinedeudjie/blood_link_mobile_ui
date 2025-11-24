package com.example.bloodlink.ui.screens.donor

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
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.ui.screens.donor.DonorProfileState
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White
import com.example.bloodlink.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBloodBankScreen(
    onBloodBankSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Get all registered blood banks from UserDataStore
    val availableBloodBanks = remember {
        UserDataStore.getAllBloodBanks()
    }
    
    val filteredBloodBanks = remember(searchQuery, availableBloodBanks) {
        if (searchQuery.isBlank()) {
            availableBloodBanks
        } else {
            availableBloodBanks.filter {
                it.bloodBankName.contains(searchQuery, ignoreCase = true) ||
                it.address.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true)
            }
        }
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
                        Text("Select Blood Bank")
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
            Text(
                text = "Select a blood bank to affiliate with. You'll receive donation alerts from this blood bank.",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Search bar
            BloodLinkTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search Blood Banks",
                placeholder = "Search by name or address...",
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Search
            )

            // Blood Bank List
            if (availableBloodBanks.isEmpty()) {
                // No blood banks registered in the system
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = "No Blood Banks",
                        tint = GrayMedium,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Blood Banks Available",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "There are no blood banks registered in the system yet. Blood banks will appear here once they register.",
                        fontSize = 14.sp,
                        color = GrayMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else if (filteredBloodBanks.isEmpty()) {
                // Search returned no results
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "No Results",
                        tint = GrayMedium,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No blood banks found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    Text(
                        text = "Try a different search term",
                        fontSize = 14.sp,
                        color = GrayMedium
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredBloodBanks) { bloodBankData ->
                        BloodBankCard(
                            bloodBankData = bloodBankData,
                            onAffiliateClick = {
                                // Check if already added by email
                                val bloodBankEmail = bloodBankData.email.lowercase()
                                if (!DonorProfileState.bloodBankAffiliationEmails.contains(bloodBankEmail)) {
                                    DonorProfileState.bloodBankAffiliationEmails.add(bloodBankEmail)
                                    
                                    // Also save to UserDataStore for persistence
                                    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
                                    if (currentEmail != null) {
                                        com.example.bloodlink.data.UserDataStore.saveDonorBloodBankAffiliationEmails(
                                            currentEmail, 
                                            DonorProfileState.bloodBankAffiliationEmails.toList()
                                        )
                                    }
                                }
                                onBloodBankSelected(bloodBankEmail.hashCode())
                            },
                            onUnaffiliateClick = {
                                // Remove affiliation by email
                                val bloodBankEmail = bloodBankData.email.lowercase()
                                if (DonorProfileState.bloodBankAffiliationEmails.contains(bloodBankEmail)) {
                                    DonorProfileState.bloodBankAffiliationEmails.remove(bloodBankEmail)
                                    
                                    // Also save to UserDataStore for persistence
                                    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
                                    if (currentEmail != null) {
                                        com.example.bloodlink.data.UserDataStore.saveDonorBloodBankAffiliationEmails(
                                            currentEmail, 
                                            DonorProfileState.bloodBankAffiliationEmails.toList()
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BloodBankCard(
    bloodBankData: com.example.bloodlink.ui.screens.bloodbank.BloodBankDataForm,
    onAffiliateClick: () -> Unit,
    onUnaffiliateClick: () -> Unit
) {
    // Check if already affiliated - observe the affiliations list
    val bloodBankEmail = bloodBankData.email.lowercase()
    val affiliationEmails = DonorProfileState.bloodBankAffiliationEmails
    val isAlreadyAffiliated = affiliationEmails.contains(bloodBankEmail)
    
    BloodLinkCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BloodRed.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = BloodRed,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = bloodBankData.bloodBankName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Text(
                            text = bloodBankData.address,
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                }
                if (isAlreadyAffiliated) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                        IconButton(
                            onClick = onUnaffiliateClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Affiliation",
                                tint = com.example.bloodlink.ui.theme.ErrorRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = onAffiliateClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Affiliation",
                            tint = BloodRed
                        )
                    }
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = GrayMedium,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = bloodBankData.phone.ifBlank { "Not provided" },
                        fontSize = 12.sp,
                        color = GrayMedium
                    )
                }
                if (bloodBankData.licenseNumber.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Licensed",
                            fontSize = 12.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

