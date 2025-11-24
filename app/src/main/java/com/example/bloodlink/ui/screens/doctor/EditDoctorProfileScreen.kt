package com.example.bloodlink.ui.screens.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkDropdown
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDoctorProfileScreen(
    onSaveClick: (String, String, String, String, String, String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Load existing data from DoctorProfileState
    val existingData = DoctorProfileState.savedDoctorData
    
    var firstName by remember { mutableStateOf(existingData?.firstName ?: "") }
    var lastName by remember { mutableStateOf(existingData?.lastName ?: "") }
    var email by remember { mutableStateOf(existingData?.email ?: "") }
    var phone by remember { mutableStateOf(existingData?.phone ?: "") }
    var hospitalName by remember { mutableStateOf(existingData?.hospitalName ?: "") }
    var specialization by remember { mutableStateOf(existingData?.specialization ?: "") }
    var medicalLicenseNumber by remember { mutableStateOf(existingData?.medicalLicenseNumber ?: "") }

    val specializationOptions = listOf(
        "General Medicine",
        "Cardiology",
        "Hematology",
        "Emergency Medicine",
        "Internal Medicine",
        "Surgery",
        "Pediatrics",
        "Other"
    )

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
                    // Personal Information Section
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BloodLinkTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = "First Name *",
                            placeholder = "Enter first name",
                            modifier = Modifier.weight(1f)
                        )
                        BloodLinkTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = "Last Name *",
                            placeholder = "Enter last name",
                            modifier = Modifier.weight(1f)
                        )
                    }

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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Professional Information Section
                    Text(
                        text = "Professional Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    BloodLinkTextField(
                        value = hospitalName,
                        onValueChange = { hospitalName = it },
                        label = "Hospital Name *",
                        placeholder = "Enter hospital name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    BloodLinkDropdown(
                        value = specialization,
                        onValueChange = { specialization = it },
                        label = "Specialization *",
                        options = specializationOptions,
                        placeholder = "Select specialization",
                        modifier = Modifier.fillMaxWidth()
                    )

                    BloodLinkTextField(
                        value = medicalLicenseNumber,
                        onValueChange = { medicalLicenseNumber = it },
                        label = "Medical License Number *",
                        placeholder = "Enter license number",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val isValid = firstName.isNotBlank() &&
                            lastName.isNotBlank() &&
                            email.isNotBlank() &&
                            phone.isNotBlank() &&
                            hospitalName.isNotBlank() &&
                            specialization.isNotBlank() &&
                            medicalLicenseNumber.isNotBlank()

                    BloodLinkButton(
                        text = "Save Changes",
                        onClick = {
                            // Save to DoctorProfileState
                            DoctorProfileState.savedDoctorData = DoctorDataForm(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                phone = phone,
                                hospitalName = hospitalName,
                                specialization = specialization,
                                medicalLicenseNumber = medicalLicenseNumber
                            )
                            onSaveClick(
                                firstName,
                                lastName,
                                email,
                                phone,
                                hospitalName,
                                specialization,
                                medicalLicenseNumber
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

