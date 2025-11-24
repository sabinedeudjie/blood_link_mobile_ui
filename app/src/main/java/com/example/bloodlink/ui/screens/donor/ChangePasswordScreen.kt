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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.ErrorRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onSaveClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    
    // Get current user email and role from AuthState
    val currentEmail = AuthState.currentUserEmail
    val currentRole = AuthState.currentUserRole
    
    // Get stored password for the current user
    val storedPassword = remember(currentEmail, currentRole) {
        if (currentEmail != null && currentRole != null) {
            UserDataStore.getPassword(currentEmail, currentRole) ?: ""
        } else {
            ""
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
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Blood Link",
                            tint = White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Password")
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
                .background(White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Update your password to keep your account secure",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BloodLinkTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Current Password *",
                        placeholder = "Enter your current password",
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    BloodLinkTextField(
                        value = newPassword,
                        onValueChange = { 
                            newPassword = it
                            // Clear error message when user starts typing
                            if (errorMessage.isNotBlank()) errorMessage = ""
                        },
                        label = "New Password *",
                        placeholder = "Enter your new password (min 6 characters)",
                        modifier = Modifier.fillMaxWidth(),
                        isError = newPassword.isNotBlank() && newPassword.length < 6,
                        errorMessage = if (newPassword.isNotBlank() && newPassword.length < 6) "Password must be at least 6 characters" else "",
                        visualTransformation = PasswordVisualTransformation()
                    )

                    BloodLinkTextField(
                        value = confirmPassword,
                        onValueChange = { 
                            confirmPassword = it
                            // Clear error message when user starts typing
                            if (errorMessage.isNotBlank()) errorMessage = ""
                        },
                        label = "Confirm New Password *",
                        placeholder = "Confirm your new password",
                        modifier = Modifier.fillMaxWidth(),
                        isError = confirmPassword.isNotBlank() && newPassword != confirmPassword,
                        errorMessage = if (confirmPassword.isNotBlank() && newPassword != confirmPassword) "Passwords do not match" else "",
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Show error message if any
                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = ErrorRed,
                            fontSize = 14.sp
                        )
                    }
                    
                    // Show success message if any
                    if (successMessage.isNotBlank()) {
                        Text(
                            text = successMessage,
                            color = SuccessGreen,
                            fontSize = 14.sp
                        )
                    }

                    // Validation: Enable button when all fields are valid
                    val isValid = remember(currentPassword, newPassword, confirmPassword) {
                        currentPassword.isNotBlank() &&
                        newPassword.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        newPassword == confirmPassword &&
                        newPassword.length >= 6 &&
                        newPassword != currentPassword // New password must be different from current
                    }

                    BloodLinkButton(
                        text = "Change Password",
                        onClick = {
                            // Clear previous messages
                            errorMessage = ""
                            successMessage = ""
                            
                            // Verify current password exists
                            if (storedPassword.isBlank()) {
                                errorMessage = "No password found. Please register first."
                                return@BloodLinkButton
                            }
                            
                            // Verify current password is correct
                            if (currentPassword != storedPassword) {
                                errorMessage = "Current password is incorrect"
                                return@BloodLinkButton
                            }
                            
                            // Verify new password is different from current
                            if (newPassword == currentPassword) {
                                errorMessage = "New password must be different from current password"
                                return@BloodLinkButton
                            }
                            
                            // Verify new password meets requirements
                            if (newPassword.length < 6) {
                                errorMessage = "New password must be at least 6 characters"
                                return@BloodLinkButton
                            }
                            
                            // Verify passwords match
                            if (newPassword != confirmPassword) {
                                errorMessage = "New password and confirmation do not match"
                                return@BloodLinkButton
                            }

                            // Verify user is logged in
                            if (currentEmail == null || currentRole == null) {
                                errorMessage = "Password doesn't exist"
                                return@BloodLinkButton
                            }
                            
                            isLoading = true
                            
                            // Update password in UserDataStore using the generic method
                            UserDataStore.updatePassword(currentEmail, newPassword, currentRole, )
                            
                            // Update password in the corresponding profile state
                            when (currentRole) {
                                com.example.bloodlink.data.model.enums.UserRole.DONOR -> {
                                    com.example.bloodlink.ui.screens.donor.DonorProfileState.password = newPassword
                                }
                                com.example.bloodlink.data.model.enums.UserRole.DOCTOR -> {
                                    com.example.bloodlink.ui.screens.doctor.DoctorProfileState.password = newPassword
                                }
                                com.example.bloodlink.data.model.enums.UserRole.BLOOD_BANK -> {
                                    com.example.bloodlink.ui.screens.bloodbank.BloodBankProfileState.password = newPassword
                                }
                            }
                            
                            isLoading = false
                            successMessage = "Password changed successfully!"
                            
                            // Call the callback and navigate back after a short delay to show success message
                            coroutineScope.launch {
                                delay(1500) // Show success message for 1.5 seconds
                                onSaveClick(currentPassword, newPassword, confirmPassword)
                            }
                        },
                        enabled = !isLoading && isValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

