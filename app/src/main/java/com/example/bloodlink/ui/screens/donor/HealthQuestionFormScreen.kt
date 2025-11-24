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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.White
import com.example.bloodlink.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthQuestionFormScreen(
    hasTattoosWithinLast6Months: Boolean = false,
    hasSurgeryWithinLast6_12Months: Boolean = false,
    hasChronicalIllness: Boolean = false,
    hasTravelledWithinLast3Months: Boolean = false,
    hasPiercingWithinLast7Months: Boolean = false,
    isOnMedication: Boolean = false,
    onSaveClick: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get donor's gender to show gender-specific questions
    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
    val personalData = remember(currentEmail) {
        currentEmail?.let { com.example.bloodlink.data.UserDataStore.getDonorPersonalData(it) }
            ?: DonorProfileState.savedPersonalData
    }
    val isFemale = personalData?.gender == com.example.bloodlink.data.model.enums.Gender.FEMALE
    
    // Load saved answers if available
    val savedAnswers = remember { DonorProfileState.savedHealthQuestions }
    
    var hasTattoosState by remember(hasTattoosWithinLast6Months) { 
        mutableStateOf(savedAnswers?.get("hasTattoosWithinLast6Months") ?: hasTattoosWithinLast6Months)
    }
    var hasSurgeryState by remember(hasSurgeryWithinLast6_12Months) { 
        mutableStateOf(savedAnswers?.get("hasSurgeryWithinLast6_12Months") ?: hasSurgeryWithinLast6_12Months)
    }
    var hasChronicalIllnessState by remember(hasChronicalIllness) { 
        mutableStateOf(savedAnswers?.get("hasChronicalIllness") ?: hasChronicalIllness)
    }
    var hasTravelledState by remember(hasTravelledWithinLast3Months) { 
        mutableStateOf(savedAnswers?.get("hasTravelledWithinLast3Months") ?: hasTravelledWithinLast3Months)
    }
    var hasPiercingState by remember(hasPiercingWithinLast7Months) { 
        mutableStateOf(savedAnswers?.get("hasPiercingWithinLast7Months") ?: hasPiercingWithinLast7Months)
    }
    var isOnMedicationState by remember(isOnMedication) { 
        mutableStateOf(savedAnswers?.get("isOnMedication") ?: isOnMedication)
    }
    
    // Female-specific questions
    var isPregnantState by remember { 
        mutableStateOf(savedAnswers?.get("isPregnant") ?: false)
    }
    var isBreastFeedingState by remember { 
        mutableStateOf(savedAnswers?.get("isBreastFeeding") ?: false)
    }
    var isChildBirthWithinLast3MonthsState by remember { 
        mutableStateOf(savedAnswers?.get("isChildBirthWithinLast3Months") ?: false)
    }
    var hasHeavyMenstrualFlowState by remember { 
        mutableStateOf(savedAnswers?.get("hasHeavyMenstrualFlow") ?: false)
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
                        Text("Health Questions")
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
                text = "Please answer the following health questions honestly. This information is crucial for ensuring safe blood donation." +
                        if (isFemale) "\n\nAdditional questions specific to female donors are shown below." else "",
                fontSize = 14.sp,
                color = com.example.bloodlink.ui.theme.GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (personalData?.gender == null) {
                Text(
                    text = "Note: Please set your gender in Personal Information to see gender-specific questions.",
                    fontSize = 12.sp,
                    color = com.example.bloodlink.ui.theme.ErrorRed,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Health questions matching HealthQuestions model
            HealthQuestionCard(
                question = "Have you had any tattoos within the last 6 months?",
                answer = hasTattoosState,
                onAnswerChange = { hasTattoosState = it }
            )

            HealthQuestionCard(
                question = "Have you had any surgery within the last 6-12 months?",
                answer = hasSurgeryState,
                onAnswerChange = { hasSurgeryState = it }
            )

            HealthQuestionCard(
                question = "Do you have any chronic illness?",
                answer = hasChronicalIllnessState,
                onAnswerChange = { hasChronicalIllnessState = it }
            )

            HealthQuestionCard(
                question = "Have you travelled within the last 3 months?",
                answer = hasTravelledState,
                onAnswerChange = { hasTravelledState = it }
            )

            HealthQuestionCard(
                question = "Have you had any piercing within the last 7 months?",
                answer = hasPiercingState,
                onAnswerChange = { hasPiercingState = it }
            )

            HealthQuestionCard(
                question = "Are you currently on any medication?",
                answer = isOnMedicationState,
                onAnswerChange = { isOnMedicationState = it }
            )

            // Female-specific questions (only show if gender is FEMALE)
            if (isFemale) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Female-Specific Questions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.bloodlink.ui.theme.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                HealthQuestionCard(
                    question = "Are you currently pregnant?",
                    answer = isPregnantState,
                    onAnswerChange = { isPregnantState = it }
                )

                HealthQuestionCard(
                    question = "Are you currently breastfeeding?",
                    answer = isBreastFeedingState,
                    onAnswerChange = { isBreastFeedingState = it }
                )

                HealthQuestionCard(
                    question = "Have you given birth within the last 3 months?",
                    answer = isChildBirthWithinLast3MonthsState,
                    onAnswerChange = { isChildBirthWithinLast3MonthsState = it }
                )

                HealthQuestionCard(
                    question = "Do you have heavy menstrual flow?",
                    answer = hasHeavyMenstrualFlowState,
                    onAnswerChange = { hasHeavyMenstrualFlowState = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            BloodLinkButton(
                text = "Save Answers",
                onClick = {
                    // Save to shared state as Map matching HealthQuestions properties
                    val healthQuestionsMap = mutableMapOf<String, Boolean>(
                        "hasTattoosWithinLast6Months" to hasTattoosState,
                        "hasSurgeryWithinLast6_12Months" to hasSurgeryState,
                        "hasChronicalIllness" to hasChronicalIllnessState,
                        "hasTravelledWithinLast3Months" to hasTravelledState,
                        "hasPiercingWithinLast7Months" to hasPiercingState,
                        "isOnMedication" to isOnMedicationState
                    )
                    
                    // Add female-specific questions if gender is FEMALE
                    if (isFemale) {
                        healthQuestionsMap["isPregnant"] = isPregnantState
                        healthQuestionsMap["isBreastFeeding"] = isBreastFeedingState
                        healthQuestionsMap["isChildBirthWithinLast3Months"] = isChildBirthWithinLast3MonthsState
                        healthQuestionsMap["hasHeavyMenstrualFlow"] = hasHeavyMenstrualFlowState
                    }
                    
                    DonorProfileState.savedHealthQuestions = healthQuestionsMap
                    
                    // Also save to UserDataStore for persistence
                    val currentEmail = com.example.bloodlink.data.AuthState.currentUserEmail
                    if (currentEmail != null) {
                        com.example.bloodlink.data.UserDataStore.saveDonorHealthQuestions(currentEmail, healthQuestionsMap)
                    }
                    
                    onSaveClick(
                        hasTattoosState,
                        hasSurgeryState,
                        hasChronicalIllnessState,
                        hasTravelledState,
                        hasPiercingState,
                        isOnMedicationState
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = true // All questions can be answered
            )
        }
    }
}

@Composable
fun HealthQuestionCard(
    question: String,
    answer: Boolean,
    onAnswerChange: (Boolean) -> Unit
) {
    BloodLinkCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = answer == true,
                        onClick = { onAnswerChange(true) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BloodRed
                        )
                    )
                    Text(
                        text = "Yes",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = answer == false,
                        onClick = { onAnswerChange(false) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BloodRed
                        )
                    )
                    Text(
                        text = "No",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                }
            }
        }
    }
}
