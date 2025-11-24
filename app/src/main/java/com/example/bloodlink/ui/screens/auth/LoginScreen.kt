package com.example.bloodlink.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.dtos.requests.LoginRequest
import com.example.bloodlink.data.model.metiers.User
import com.example.bloodlink.retrofit.RetrofitInstance
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    loginError: String?,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    //val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        // Back to Home button
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
                text = "Back",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.clickable(onClick = onBackClick)
            )
        }

        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Welcome Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Sign in to your account to continue",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Login Card
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
                    .padding(24.dp)
            ) {
                BloodLinkTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Enter your email",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                BloodLinkTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Enter your password",
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Show error message if login fails
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = com.example.bloodlink.ui.theme.ErrorRed,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Remember me and Forgot password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = BloodRed
                            )
                        )
                        Text(
                            text = "Remember me",
                            fontSize = 14.sp,
                            color = com.example.bloodlink.ui.theme.Black
                        )
                    }
                    Text(
                        text = "Forgot password?",
                        fontSize = 14.sp,
                        color = BloodRed,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable(onClick = onForgotPasswordClick)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                BloodLinkButton(
                    text = "Sign In",
                    onClick = {
                        isLoading = true
                        onLoginClick(email, password)
                        errorMessage = ""
                    },
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign up link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 14.sp,
                color = GrayMedium
            )
            Text(
                text = "Register here",
                fontSize = 14.sp,
                color = BloodRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onSignUpClick)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
