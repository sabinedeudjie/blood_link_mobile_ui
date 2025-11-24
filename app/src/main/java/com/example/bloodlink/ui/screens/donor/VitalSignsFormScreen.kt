package com.example.bloodlink.ui.screens.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalSignsFormScreen(
    hemoglobinLevel: Float = 0f,
    bodyTemperature: Float = 0f,
    pulseRate: Float = 0f,
    onSaveClick: (Float, Float, Float) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hemoglobinLevelState by remember(hemoglobinLevel) { 
        mutableStateOf(if (hemoglobinLevel > 0f) hemoglobinLevel.toString() else "")
    }
    var bodyTemperatureState by remember(bodyTemperature) { 
        mutableStateOf(if (bodyTemperature > 0f) bodyTemperature.toString() else "")
    }
    var pulseRateState by remember(pulseRate) { 
        mutableStateOf(if (pulseRate > 0f) pulseRate.toString() else "")
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
                        Text("Vital Signs")
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
                        text = "Current Vital Signs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    
                    Text(
                        text = "These measurements are used to assess your eligibility for blood donation.",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.GrayMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Hemoglobin Level field (Float)
                    BloodLinkTextField(
                        value = hemoglobinLevelState,
                        onValueChange = { 
                            // Only allow numeric input
                            if (it.isEmpty() || it.matches(Regex("^\\d+(\\.\\d*)?$"))) {
                                hemoglobinLevelState = it
                            }
                        },
                        label = "Hemoglobin Level (g/dL) *",
                        placeholder = "Enter hemoglobin level (minimum 12.0 g/dL for donation)",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Decimal
                    )

                    // Body Temperature field (Float)
                    BloodLinkTextField(
                        value = bodyTemperatureState,
                        onValueChange = { 
                            // Only allow numeric input
                            if (it.isEmpty() || it.matches(Regex("^\\d+(\\.\\d*)?$"))) {
                                bodyTemperatureState = it
                            }
                        },
                        label = "Body Temperature (°C) *",
                        placeholder = "Enter body temperature (normal: 37-38°C)",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Decimal
                    )

                    // Pulse Rate field (Float)
                    BloodLinkTextField(
                        value = pulseRateState,
                        onValueChange = { 
                            // Only allow numeric input
                            if (it.isEmpty() || it.matches(Regex("^\\d+(\\.\\d*)?$"))) {
                                pulseRateState = it
                            }
                        },
                        label = "Pulse Rate (bpm) *",
                        placeholder = "Enter pulse rate (normal: 50-100 bpm)",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BloodLinkButton(
                        text = "Save Changes",
                        onClick = {
                            // Convert strings to Float
                            val hemoglobinFloat = hemoglobinLevelState.toFloatOrNull() ?: 0f
                            val temperatureFloat = bodyTemperatureState.toFloatOrNull() ?: 0f
                            val pulseFloat = pulseRateState.toFloatOrNull() ?: 0f
                            
                            // Save to shared state
                            val vitalSigns = VitalSignsForm(
                                hemoglobinLevel = hemoglobinFloat,
                                bodyTemperature = temperatureFloat,
                                pulseRate = pulseFloat
                            )
                            DonorProfileState.savedVitalSigns = vitalSigns
                            
                            // Also save to UserDataStore for persistence
                            val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
                            if (currentEmail != null) {
                                com.example.bloodlink.data.UserDataStore.saveDonorVitalSigns(currentEmail, vitalSigns)
                            }
                            
                            onSaveClick(
                                hemoglobinFloat,
                                temperatureFloat,
                                pulseFloat
                            )
                        },
                        enabled = hemoglobinLevelState.isNotBlank() &&
                                hemoglobinLevelState.toFloatOrNull() != null &&
                                bodyTemperatureState.isNotBlank() &&
                                bodyTemperatureState.toFloatOrNull() != null &&
                                pulseRateState.isNotBlank() &&
                                pulseRateState.toFloatOrNull() != null
                    )
                }
            }
        }
    }
}
