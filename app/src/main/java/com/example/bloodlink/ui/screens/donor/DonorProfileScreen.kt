package com.example.bloodlink.ui.screens.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.appl.Donation
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.metiers.BloodBank
import com.example.bloodlink.data.model.metiers.Donor
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.DonorBottomNavigation
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White

// Data classes for form data
// DonorDataForm stores contact information
data class DonorDataForm(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val bloodType: com.example.bloodlink.data.model.enums.BloodType? = null
)

// PersonalDataForm corresponds to PersonalInfos model
data class PersonalDataForm(
    val birthdate: java.time.LocalDate? = null, // Corresponds to PersonalInfos.birthdate
    val gender: com.example.bloodlink.data.model.enums.Gender? = null, // Corresponds to PersonalInfos.gender
    val weight: Float = 0f, // Corresponds to PersonalInfos.weight
    val emergencyContact: String = "" // Corresponds to PersonalInfos.emergencyContact
)

// VitalSignsForm corresponds to VitalSigns model
data class VitalSignsForm(
    val hemoglobinLevel: Float = 0f, // Corresponds to VitalSigns.hemoglobinLevel
    val bodyTemperature: Float = 0f, // Corresponds to VitalSigns.bodyTemperature
    val pulseRate: Float = 0f // Corresponds to VitalSigns.pulseRate
)

// Shared state to persist data across navigation
object DonorProfileState {
    var savedPersonalData: PersonalDataForm? by mutableStateOf(null)
    var savedVitalSigns: VitalSignsForm? by mutableStateOf(null)
    var savedHealthQuestions: Map<String, Boolean>? by mutableStateOf(null)
    var bloodBankAffiliations: MutableList<BloodBank> = mutableStateListOf() // For backward compatibility
    var bloodBankAffiliationEmails: MutableList<String> = mutableStateListOf() // Store emails instead
    var isEligible: Boolean? by mutableStateOf(null) // null = not checked, true = eligible, false = not eligible
    var donationHistory: MutableList<Donation> = mutableStateListOf()
    var password: String by mutableStateOf("") // Store password for change password functionality
}

// Eligibility checking function - now uses VitalSigns.meetDonationCriteria() and HealthQuestions.hasDeferralIssues()
// This function is kept for backward compatibility but eligibility should be checked by blood bank
fun checkDonorEligibility(
    personalData: PersonalDataForm?,
    vitalSigns: VitalSignsForm?,
    healthQuestions: Map<String, Boolean>?,
    gender: com.example.bloodlink.data.model.enums.Gender?
): Boolean {
    // Must have all required data
    if (personalData == null || vitalSigns == null || healthQuestions == null) {
        return false
    }
    
    // Check age (must be at least 18 years old)
    if (personalData.birthdate != null) {
        val age = java.time.temporal.ChronoUnit.YEARS.between(personalData.birthdate, java.time.LocalDate.now())
        if (age < 18) return false
    }
    
    // Check vital signs using VitalSigns criteria
    // Hemoglobin must be >= 12.0 g/dL
    if (vitalSigns.hemoglobinLevel < 12.0f) return false
    
    // Body temperature must be between 37-38°C
    if (vitalSigns.bodyTemperature < 37f || vitalSigns.bodyTemperature > 38f) return false
    
    // Pulse rate must be between 50-100 bpm
    if (vitalSigns.pulseRate < 50f || vitalSigns.pulseRate > 100f) return false
    
    // Weight must be at least 50 kg
    if (personalData.weight < 50f) return false
    
    // Check health questions - use the specific properties from HealthQuestions model
    if (healthQuestions["hasTattoosWithinLast6Months"] == true) return false
    if (healthQuestions["hasSurgeryWithinLast6_12Months"] == true) return false
    if (healthQuestions["hasChronicalIllness"] == true) return false
    if (healthQuestions["hasTravelledWithinLast3Months"] == true) return false
    if (healthQuestions["hasPiercingWithinLast7Months"] == true) return false
    if (healthQuestions["isOnMedication"] == true) return false
    
    // If all checks pass, donor is eligible
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorProfileScreen(
    donor: Donor? = null,
    userName: String = "Axelle Mbadi",
    bloodType: BloodType = BloodType.O_POSITIVE,
    onNavigate: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    onHealthProfileClick: () -> Unit,
    onAddBloodBankClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Use saved state - access directly to observe changes
    val personalData by DonorProfileState::savedPersonalData
    val vitalSigns by DonorProfileState::savedVitalSigns
    // Get blood bank data from emails
    val bloodBankEmails = DonorProfileState.bloodBankAffiliationEmails
    val bloodBanks = remember(bloodBankEmails) {
        bloodBankEmails.mapNotNull { email ->
            com.example.bloodlink.data.UserDataStore.getBloodBankByEmail(email)
        }
    }
    // Get current user email for contact info
    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
    // Get donor contact data from UserDataStore
    val donorContactData = remember(currentEmail) {
        currentEmail?.let { com.example.bloodlink.data.UserDataStore.getDonorContactData(it) }
    }
    // Use contact data or fallback to parameters
    val displayName = donorContactData?.let { "${it.firstName} ${it.lastName}".trim() }
        .takeIf { !it.isNullOrBlank() } ?: userName
    val displayEmail = donorContactData?.email?.takeIf { it.isNotBlank() } ?: currentEmail ?: "Not provided"
    val displayPhone = donorContactData?.phone?.takeIf { it.isNotBlank() } ?: "Not provided"
    val displayBloodType = donorContactData?.bloodType ?: bloodType
    val healthProfileComplete = remember(personalData, vitalSigns) { 
        personalData != null && vitalSigns != null && 
        personalData!!.birthdate != null && 
        personalData!!.gender != null &&
        personalData!!.weight > 0f &&
        vitalSigns!!.hemoglobinLevel > 0f &&
        vitalSigns!!.bodyTemperature > 0f &&
        vitalSigns!!.pulseRate > 0f
    }
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
                        Text("Donor Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                )
            )
        },
        bottomBar = {
            DonorBottomNavigation(
                currentRoute = Screen.DonorProfile.route,
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
                text = "Manage your personal information, vital signs, and health details",
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

                    ProfileField(
                        label = "Name",
                        value = displayName
                    )
                    ProfileField(
                        label = "Email",
                        value = displayEmail
                    )
                    ProfileField(
                        label = "Phone",
                        value = displayPhone
                    )
                    ProfileField(
                        label = "Blood Type",
                        value = displayBloodType?.value ?: "Not provided"
                    )
                    ProfileField(
                        label = "Date of Birth",
                        value = personalData?.birthdate?.let { 
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(it)
                        } ?: "Not provided"
                    )
                    ProfileField(
                        label = "Gender",
                        value = personalData?.gender?.name ?: "Not provided"
                    )
                    ProfileField(
                        label = "Weight",
                        value = personalData?.let { 
                            if (it.weight > 0f) "${it.weight} kg" else "Not provided"
                        } ?: "Not provided"
                    )
                    ProfileField(
                        label = "Emergency Contact",
                        value = personalData?.emergencyContact?.takeIf { it.isNotBlank() } ?: "Not provided"
                    )
                }
            }

            // Health Profile Card
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
                            BloodLinkLogo(
                                size = 24.dp,
                                tint = BloodRed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Health Profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                        }
                        IconButton(onClick = onHealthProfileClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "View",
                                tint = BloodRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (healthProfileComplete) {
                        // Show health profile details
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Complete",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Health profile is complete",
                                    fontSize = 14.sp,
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            // Show vital signs if available
                            vitalSigns?.let {
                                Divider()
                                Text(
                                    text = "Vital Signs",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.bloodlink.ui.theme.Black
                                )
                                if (it.hemoglobinLevel > 0f) {
                                    ProfileField(label = "Hemoglobin Level", value = "${it.hemoglobinLevel} g/dL")
                                }
                                if (it.bodyTemperature > 0f) {
                                    ProfileField(label = "Body Temperature", value = "${it.bodyTemperature} °C")
                                }
                                if (it.pulseRate > 0f) {
                                    ProfileField(label = "Pulse Rate", value = "${it.pulseRate} bpm")
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Complete your health profile to receive donation alerts",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                }
            }

            // Blood Bank Affiliations Card
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
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
                            text = "Blood Bank Affiliations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (bloodBanks.isNotEmpty() || (donor?.affiliatedDonorBanks?.isNotEmpty() == true)) {
                        // Show saved blood banks
                        if (bloodBanks.isNotEmpty()) {
                            bloodBanks.forEach { bloodBankData ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = bloodBankData.bloodBankName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = com.example.bloodlink.ui.theme.Black
                                        )
                                        Text(
                                            text = bloodBankData.address,
                                            fontSize = 14.sp,
                                            color = GrayMedium
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            // Remove affiliation by email
                                            val email = bloodBankData.email.lowercase()
                                            if (DonorProfileState.bloodBankAffiliationEmails.contains(email)) {
                                                DonorProfileState.bloodBankAffiliationEmails.remove(email)
                                                
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
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove Affiliation",
                                            tint = com.example.bloodlink.ui.theme.ErrorRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                if (bloodBankData != bloodBanks.last()) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        } else {
                            // Fallback to donor's blood bank if no saved ones
                            Text(
                                text = "City Central Blood Bank",
                                fontSize = 16.sp,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        BloodLinkButton(
                            text = "Add Blood Bank",
                            onClick = onAddBloodBankClick
                        )
                    } else {
                        Text(
                            text = "No blood bank affiliations yet",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BloodLinkButton(
                            text = "Add Blood Bank",
                            onClick = onAddBloodBankClick
                        )
                    }
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
                    
                    // Edit Personal Information
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
                                    text = "Edit Personal Information",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = com.example.bloodlink.ui.theme.Black
                                )
                                Text(
                                    text = "Update your personal details",
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

