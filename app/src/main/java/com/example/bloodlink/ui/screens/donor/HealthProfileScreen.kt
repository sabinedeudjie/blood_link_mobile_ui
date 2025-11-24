package com.example.bloodlink.ui.screens.donor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.bloodlink.data.model.medicalProfile.MedicalProfile
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bloodlink.ui.screens.donor.checkDonorEligibility
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.UserDataStore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthProfileScreen(
    healthProfile: MedicalProfile? = null,
    onPersonalDataClick: () -> Unit,
    onVitalSignsClick: () -> Unit,
    onHealthQuestionClick: () -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get saved data from DonorProfileState
    val personalData = remember { DonorProfileState.savedPersonalData }
    val vitalSigns = remember { DonorProfileState.savedVitalSigns }
    val healthQuestions = remember { DonorProfileState.savedHealthQuestions }
    val hasPersonalData = personalData != null && personalData.birthdate != null && personalData.gender != null
    val hasVitalSigns = vitalSigns != null && vitalSigns.hemoglobinLevel > 0f && vitalSigns.bodyTemperature > 0f && vitalSigns.pulseRate > 0f
    val hasHealthQuestions = healthQuestions != null && healthQuestions.isNotEmpty()
    val isComplete = hasPersonalData && hasVitalSigns && hasHealthQuestions

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = com.example.bloodlink.ui.theme.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.bloodlink.ui.theme.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isComplete) {
                Text(
                    text = "Complete your health profile to start donating blood",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                ProfileStatusCard(isComplete = true)
            }

            // Personal Data Section with Details
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onPersonalDataClick),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Personal Data",
                                tint = BloodRed,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Personal Data",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Your basic information",
                                    fontSize = 14.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                        if (hasPersonalData) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Go to",
                                tint = GrayMedium
                            )
                        }
                    }
                    
                    // Show Personal Data details if available
                    if (hasPersonalData) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            personalData?.let { data ->
                                if (data.birthdate != null) {
                                    HealthProfileField("Date of Birth", java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data.birthdate))
                                }
                                if (data.gender != null) {
                                    HealthProfileField("Gender", data.gender.name)
                                }
                                if (data.weight > 0f) {
                                    HealthProfileField("Weight", "${data.weight} kg")
                                }
                                if (data.emergencyContact.isNotBlank()) {
                                    HealthProfileField("Emergency Contact", data.emergencyContact)
                                }
                            }
                        }
                    }
                }
            }

            // Vital Signs Section with Details
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onVitalSignsClick),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Vital Signs",
                                tint = BloodRed,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Vital Signs",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Current health metrics",
                                    fontSize = 14.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                        if (hasVitalSigns) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Go to",
                                tint = GrayMedium
                            )
                        }
                    }
                    
                    // Show Vital Signs details if available
                    if (hasVitalSigns) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            vitalSigns?.let { signs ->
                                if (signs.hemoglobinLevel > 0f) {
                                    HealthProfileField("Hemoglobin Level", "${signs.hemoglobinLevel} g/dL")
                                }
                                if (signs.bodyTemperature > 0f) {
                                    HealthProfileField("Body Temperature", "${signs.bodyTemperature} °C")
                                }
                                if (signs.pulseRate > 0f) {
                                    HealthProfileField("Pulse Rate", "${signs.pulseRate} bpm")
                                }
                            }
                        }
                    }
                }
            }

            // Health Questions Section with Details
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onHealthQuestionClick),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.QuestionAnswer,
                                contentDescription = "Health Questions",
                                tint = BloodRed,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Health Questions",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Eligibility questionnaire",
                                    fontSize = 14.sp,
                                    color = GrayMedium
                                )
                            }
                        }
                        if (hasHealthQuestions) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Go to",
                                tint = GrayMedium
                            )
                        }
                    }
                    
                    // Show Health Questions summary if available
                    if (hasHealthQuestions) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            healthQuestions?.let { questions ->
                                val answeredCount = questions.values.count { it }
                                val totalCount = questions.size
                                HealthProfileField(
                                    "Questions Answered",
                                    "$answeredCount / $totalCount"
                                )
                                Text(
                                    text = "Click to view or modify your answers",
                                    fontSize = 12.sp,
                                    color = GrayMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (isComplete) {
                // Inform the donor that eligibility will be checked by the blood bank
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = com.example.bloodlink.ui.theme.BloodRed.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = BloodRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Eligibility Verification",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.bloodlink.ui.theme.Black
                            )
                            Text(
                                text = "Your eligibility will be verified by the blood bank when you respond to an alert. Make sure your health profile is complete and up to date.",
                                fontSize = 14.sp,
                                color = GrayMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HealthProfileField(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = GrayMedium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = com.example.bloodlink.ui.theme.Black
        )
    }
}

@Composable
fun ProfileStatusCard(isComplete: Boolean) {
    BloodLinkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isComplete) "Profile Complete" else "Profile Incomplete",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isComplete) "You're ready to donate!" else "Complete all sections",
                    fontSize = 14.sp,
                    color = com.example.bloodlink.ui.theme.GrayMedium
                )
            }
            Surface(
                shape = CircleShape,
                color = if (isComplete) SuccessGreen else com.example.bloodlink.ui.theme.WarningOrange,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isComplete) Icons.Default.Check else Icons.Default.Warning,
                    contentDescription = null,
                    tint = com.example.bloodlink.ui.theme.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileSectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = com.example.bloodlink.ui.theme.White
        )
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BloodRed,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = com.example.bloodlink.ui.theme.GrayMedium
                )
            }
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = SuccessGreen
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go to",
                    tint = com.example.bloodlink.ui.theme.GrayMedium
                )
            }
        }
    }
}

