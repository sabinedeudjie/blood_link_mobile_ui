package com.example.bloodlink.ui.screens.donor

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
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.Gender
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkDropdown
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.components.DatePickerField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataFormScreen(
    firstName: String = "",
    lastName: String = "",
    email: String = "",
    phone: String = "",
    bloodType: BloodType? = null,
    birthdate: LocalDate? = null,
    gender: Gender? = null,
    weight: Float = 0f,
    emergencyContact: String = "",
    onSaveClick: (String, String, String, String, BloodType?, LocalDate?, Gender?, Float, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get existing data from UserDataStore if available
    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
    val existingContactData = remember(currentEmail) {
        currentEmail?.let { com.example.bloodlink.data.UserDataStore.getDonorContactData(it) }
    }
    val existingPersonalData = remember(currentEmail) {
        currentEmail?.let { com.example.bloodlink.data.UserDataStore.getDonorPersonalData(it) }
    }
    
    // Initialize states with existing data or parameters
    var firstNameState by remember(firstName, existingContactData) { 
        mutableStateOf(existingContactData?.firstName?.takeIf { it.isNotBlank() } ?: firstName) 
    }
    var lastNameState by remember(lastName, existingContactData) { 
        mutableStateOf(existingContactData?.lastName?.takeIf { it.isNotBlank() } ?: lastName) 
    }
    var emailState by remember(email, existingContactData) { 
        mutableStateOf(existingContactData?.email?.takeIf { it.isNotBlank() } ?: email) 
    }
    var phoneState by remember(phone, existingContactData) { 
        mutableStateOf(existingContactData?.phone?.takeIf { it.isNotBlank() } ?: phone) 
    }
    var bloodTypeState by remember(bloodType, existingContactData) { 
        mutableStateOf(existingContactData?.bloodType ?: bloodType) 
    }
    
    // Convert LocalDate to Date for DatePickerField (which uses Date)
    var dateOfBirthState by remember(birthdate, existingPersonalData) { 
        mutableStateOf<Date?>(
            (existingPersonalData?.birthdate ?: birthdate)?.let { 
                Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
            }
        )
    }
    var genderState by remember(gender, existingPersonalData) { 
        mutableStateOf(existingPersonalData?.gender ?: gender) 
    }
    var weightState by remember(weight, existingPersonalData) { 
        mutableStateOf(
            if (existingPersonalData?.weight != null && existingPersonalData.weight > 0f) 
                existingPersonalData.weight.toString() 
            else if (weight > 0f) 
                weight.toString() 
            else 
                ""
        ) 
    }
    var emergencyContactState by remember(emergencyContact, existingPersonalData) { 
        mutableStateOf(existingPersonalData?.emergencyContact?.takeIf { it.isNotBlank() } ?: emergencyContact) 
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
                        Text("Personal Information")
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
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    
                    Text(
                        text = "Update your personal and medical information. This information will be used for eligibility assessment.",
                        fontSize = 14.sp,
                        color = GrayMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // First Name field
                    BloodLinkTextField(
                        value = firstNameState,
                        onValueChange = { firstNameState = it },
                        label = "First Name *",
                        placeholder = "Enter your first name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Last Name field
                    BloodLinkTextField(
                        value = lastNameState,
                        onValueChange = { lastNameState = it },
                        label = "Last Name *",
                        placeholder = "Enter your last name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Email field
                    BloodLinkTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = "Email *",
                        placeholder = "Enter your email",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Email
                    )

                    // Phone field
                    BloodLinkTextField(
                        value = phoneState,
                        onValueChange = { phoneState = it },
                        label = "Phone *",
                        placeholder = "Enter your phone number",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Phone
                    )

                    // Blood Type dropdown
                    BloodLinkDropdown(
                        value = bloodTypeState?.value ?: "",
                        onValueChange = { selected ->
                            bloodTypeState = BloodType.fromString(selected)
                        },
                        label = "Blood Type *",
                        options = BloodType.entries.map { it.value },
                        placeholder = "Select blood type",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Date of Birth field (using DatePickerField which works with Date, then convert to LocalDate)
                    DatePickerField(
                        value = dateOfBirthState,
                        onValueChange = { dateOfBirthState = it },
                        label = "Date of Birth *",
                        placeholder = "Select date of birth",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Gender dropdown
                    BloodLinkDropdown(
                        value = genderState?.name ?: "",
                        onValueChange = { selected ->
                            genderState = Gender.valueOf(selected)
                        },
                        label = "Gender *",
                        options = Gender.values().map { it.name },
                        placeholder = "Select gender",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Weight field (Float)
                    BloodLinkTextField(
                        value = weightState,
                        onValueChange = { 
                            // Only allow numeric input
                            if (it.isEmpty() || it.matches(Regex("^\\d+(\\.\\d*)?$"))) {
                                weightState = it
                            }
                        },
                        label = "Weight (kg) *",
                        placeholder = "Enter weight in kilograms",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Decimal
                    )

                    // Emergency Contact field
                    BloodLinkTextField(
                        value = emergencyContactState,
                        onValueChange = { emergencyContactState = it },
                        label = "Emergency Contact *",
                        placeholder = "Enter emergency contact name and phone",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BloodLinkButton(
                        text = "Save Changes",
                        onClick = {
                            // Convert Date back to LocalDate
                            val birthdateLocalDate = dateOfBirthState?.let { date ->
                                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            }
                            
                            // Convert weight string to Float
                            val weightFloat = weightState.toFloatOrNull() ?: 0f
                            
                            // Create contact data
                            val contactData = DonorDataForm(
                                firstName = firstNameState,
                                lastName = lastNameState,
                                email = emailState,
                                phone = phoneState,
                                bloodType = bloodTypeState
                            )
                            
                            // Create personal data
                            val personalData = PersonalDataForm(
                                birthdate = birthdateLocalDate,
                                gender = genderState,
                                weight = weightFloat,
                                emergencyContact = emergencyContactState
                            )
                            
                            // Save to shared state
                            DonorProfileState.savedPersonalData = personalData
                            
                            // Also save to UserDataStore for persistence
                            val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
                            if (currentEmail != null) {
                                val currentPassword = DonorProfileState.password
                                com.example.bloodlink.data.UserDataStore.saveDonorData(
                                    currentEmail, 
                                    contactData, 
                                    personalData, 
                                    currentPassword
                                )
                                
                                // Update email in AuthState if it changed
                                if (emailState.lowercase() != currentEmail.lowercase()) {
                                    com.example.bloodlink.data.AuthState.currentUserEmail = emailState
                                }
                            }
                            
                            onSaveClick(
                                firstNameState,
                                lastNameState,
                                emailState,
                                phoneState,
                                bloodTypeState,
                                birthdateLocalDate,
                                genderState,
                                weightFloat,
                                emergencyContactState
                            )
                        },
                        enabled = firstNameState.isNotBlank() &&
                                lastNameState.isNotBlank() &&
                                emailState.isNotBlank() &&
                                phoneState.isNotBlank() &&
                                bloodTypeState != null &&
                                dateOfBirthState != null &&
                                genderState != null &&
                                weightState.isNotBlank() &&
                                weightState.toFloatOrNull() != null &&
                                weightState.toFloatOrNull()!! > 0f &&
                                emergencyContactState.isNotBlank()
                    )
                }
            }
        }
    }
}
