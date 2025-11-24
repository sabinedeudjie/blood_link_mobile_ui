package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.NotificationState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkDropdown
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomAlertScreen(
    onBackClick: () -> Unit,
    onAlertSent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var bloodType by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var customMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var alertSent by remember { mutableStateOf(false) }
    var sendToAllAffiliated by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    
    val currentBloodBankEmail = AuthState.currentUserEmail ?: ""
    val bloodBankData = remember(currentBloodBankEmail) {
        UserDataStore.getBloodBankByEmail(currentBloodBankEmail)
    }
    val bloodBankName = bloodBankData?.bloodBankName ?: "Blood Bank"
    
    // Get affiliated donors
    val affiliatedDonors = remember(currentBloodBankEmail) {
        UserDataStore.getAffiliatedDonors(currentBloodBankEmail)
    }
    
    // Get eligible donors by blood type if blood type is selected
    val eligibleDonorEmails = remember(bloodType, affiliatedDonors) {
        if (bloodType.isBlank()) {
            // If no blood type selected, return all affiliated donors
            affiliatedDonors.map { it.first }
        } else {
            val selectedBloodType = BloodType.fromString(bloodType)
            if (selectedBloodType != null) {
                // Filter affiliated donors by blood type and eligibility
                val eligibleByType = UserDataStore.getEligibleDonorsByBloodType(selectedBloodType)
                val affiliatedEmails = affiliatedDonors.map { it.first }
                // Return only donors who are both affiliated and eligible with matching blood type
                eligibleByType.filter { it in affiliatedEmails }
            } else {
                affiliatedDonors.map { it.first }
            }
        }
    }
    
    val bloodTypeOptions = listOf("All Types") + BloodType.entries.map { it.value }

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
                        Text("Create Custom Alert")
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Send Alert to Affiliated Donors",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Create and send a custom alert to your affiliated donors",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BloodLinkCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Blood Type Selection (Optional)
                    BloodLinkDropdown(
                        value = bloodType,
                        onValueChange = { bloodType = it },
                        label = "Blood Type (Optional)",
                        options = bloodTypeOptions,
                        placeholder = "Select blood type or leave blank for all",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Quantity (Optional)
                    BloodLinkTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = "Quantity Needed (Optional)",
                        placeholder = "Enter number of units",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number
                    )

                    // Custom Message
                    BloodLinkTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        label = "Alert Message *",
                        placeholder = "Enter your alert message...",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    // Recipients Info
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = com.example.bloodlink.ui.theme.BloodRed.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Recipients",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = com.example.bloodlink.ui.theme.Black
                                    )
                                    Text(
                                        text = if (bloodType.isBlank() || bloodType == "All Types") {
                                            "${affiliatedDonors.size} affiliated donors"
                                        } else {
                                            "${eligibleDonorEmails.size} eligible affiliated donors with ${bloodType} blood"
                                        },
                                        fontSize = 12.sp,
                                        color = GrayMedium
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = BloodRed,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val isValid = customMessage.isNotBlank() && eligibleDonorEmails.isNotEmpty()

                    if (alertSent) {
                        // Success Message
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = SuccessGreen.copy(alpha = 0.1f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Alert Sent Successfully!",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                    Text(
                                        text = "Notifications have been sent to ${eligibleDonorEmails.size} donors",
                                        fontSize = 14.sp,
                                        color = GrayMedium
                                    )
                                }
                            }
                        }
                    } else {
                        BloodLinkButton(
                            text = "Send Alert",
                            onClick = {
                                if (isValid) {
                                    isLoading = true
                                    
                                    // Build message
                                    val message = if (customMessage.isNotBlank()) {
                                        customMessage
                                    } else {
                                        val bloodTypeText = if (bloodType.isNotBlank() && bloodType != "All Types") {
                                            "$bloodType blood needed"
                                        } else {
                                            "Blood donation needed"
                                        }
                                        val quantityText = if (quantity.isNotBlank()) {
                                            " ($quantity units)"
                                        } else {
                                            ""
                                        }
                                        "Urgent: $bloodTypeText$quantityText! $bloodBankName is requesting donations. Please consider donating."
                                    }
                                    
                                    // Send custom alert
                                    NotificationState.sendCustomAlert(
                                        donorEmails = eligibleDonorEmails,
                                        message = message,
                                        bloodBankName = bloodBankName,
                                        bloodBankEmail = currentBloodBankEmail,
                                        bloodType = if (bloodType.isNotBlank() && bloodType != "All Types") bloodType else null,
                                        quantity = quantity.toIntOrNull()
                                    )
                                    
                                    isLoading = false
                                    alertSent = true
                                    
                                    // Call callback after a delay
                                    coroutineScope.launch {
                                        delay(2000)
                                        onAlertSent()
                                    }
                                }
                            },
                            enabled = !isLoading && isValid,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            if (eligibleDonorEmails.isEmpty()) {
                BloodLinkCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOff,
                            contentDescription = null,
                            tint = GrayMedium,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Eligible Donors Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (affiliatedDonors.isEmpty()) {
                                "You don't have any affiliated donors yet. Donors will appear here once they affiliate with your blood bank."
                            } else if (bloodType.isNotBlank() && bloodType != "All Types") {
                                "There are no eligible affiliated donors with $bloodType blood type at the moment."
                            } else {
                                "There are no eligible affiliated donors at the moment."
                            },
                            fontSize = 14.sp,
                            color = GrayMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

