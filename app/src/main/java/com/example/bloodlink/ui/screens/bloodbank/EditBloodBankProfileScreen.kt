package com.example.bloodlink.ui.screens.bloodbank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBloodBankProfileScreen(
    onSaveClick: (String, String, String, String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Load existing data from BloodBankProfileState
    val existingData = BloodBankProfileState.savedBloodBankData
    
    var bloodBankName by remember { mutableStateOf(existingData?.bloodBankName ?: "") }
    var email by remember { mutableStateOf(existingData?.email ?: "") }
    var phone by remember { mutableStateOf(existingData?.phone ?: "") }
    var address by remember { mutableStateOf(existingData?.address ?: "") }
    var licenseNumber by remember { mutableStateOf(existingData?.licenseNumber ?: "") }

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
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Blood Link",
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile")
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Blood Bank Information Section
                    Text(
                        text = "Blood Bank Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    BloodLinkTextField(
                        value = bloodBankName,
                        onValueChange = { bloodBankName = it },
                        label = "Blood Bank Name *",
                        placeholder = "Enter blood bank name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    BloodLinkTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email *",
                        placeholder = "Enter your email",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Email
                    )

                    BloodLinkTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number *",
                        placeholder = "Enter phone number",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Phone
                    )

                    BloodLinkTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Address *",
                        placeholder = "Enter blood bank address",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // License Information Section
                    Text(
                        text = "License Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    BloodLinkTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = "License Number *",
                        placeholder = "Enter license number",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val isValid = bloodBankName.isNotBlank() &&
                            email.isNotBlank() &&
                            phone.isNotBlank() &&
                            address.isNotBlank() &&
                            licenseNumber.isNotBlank()

                    BloodLinkButton(
                        text = "Save Changes",
                        onClick = {
                            // Save to BloodBankProfileState
                            BloodBankProfileState.savedBloodBankData = BloodBankDataForm(
                                bloodBankName = bloodBankName,
                                email = email,
                                phone = phone,
                                address = address,
                                licenseNumber = licenseNumber
                            )
                            onSaveClick(
                                bloodBankName,
                                email,
                                phone,
                                address,
                                licenseNumber
                            )
                        },
                        enabled = isValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}



