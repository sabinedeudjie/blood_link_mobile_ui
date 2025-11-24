package com.example.bloodlink.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@Composable
fun ForgotPasswordScreen(
    onSendResetLinkClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GrayMedium
                )
            }
            Text(
                text = "Back to Login",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.clickable(onClick = onBackClick)
            )
        }

        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                BloodLinkLogo(
                    size = 32.dp,
                    tint = BloodRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Blood Link",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BloodRed
                )
            }

            Text(
                text = "Forgot Password",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Enter your email address and we'll send you a link to reset your password",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Forgot Password Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
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
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "Enter your email address",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(8.dp))

                BloodLinkButton(
                    text = "Send Reset Link",
                    onClick = {
                        isLoading = true
                        onSendResetLinkClick(email)
                    },
                    enabled = !isLoading && email.isNotBlank()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Login link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Remember your password? ",
                        fontSize = 14.sp,
                        color = GrayMedium
                    )
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        color = BloodRed,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable(onClick = onLoginClick)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

