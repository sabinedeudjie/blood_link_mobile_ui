package com.example.bloodlink.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = "Back to Home",
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
            Text(
                text = "Join Blood Link",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Choose your role to get started.",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Role Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RoleCard(
                title = "Donor",
                description = "Register as a donor to receive alerts when your blood type is needed and help save lives in your community.",
                icon = Icons.Default.Favorite,
                buttonText = "Register as Donor",
                buttonColor = BloodRed,
                iconBackgroundColor = BloodRed.copy(alpha = 0.15f),
                onClick = { onRoleSelected(UserRole.DONOR) }
            )

            RoleCard(
                title = "Doctor",
                description = "Medical professionals can request blood units for patients.",
                icon = Icons.Default.LocalHospital,
                buttonText = "Register as Doctor",
                buttonColor = Color(0xFF2563EB), // Blue color for medical professionals
                iconBackgroundColor = Color(0xFF2563EB).copy(alpha = 0.15f),
                onClick = { onRoleSelected(UserRole.DOCTOR) }
            )

            RoleCard(
                title = "Blood Bank",
                description = "Blood bank administrators can coordinate between Doctors and donors to fulfill blood requests.",
                icon = Icons.Default.Business,
                buttonText = "Register as Blood Bank",
                buttonColor = Color(0xFF059669), // Green color for blood banks
                iconBackgroundColor = Color(0xFF059669).copy(alpha = 0.15f),
                onClick = { onRoleSelected(UserRole.BLOOD_BANK) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 14.sp,
                color = GrayMedium
            )
            Text(
                text = "Sign in",
                fontSize = 14.sp,
                color = BloodRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String,
    buttonColor: androidx.compose.ui.graphics.Color,
    iconBackgroundColor: androidx.compose.ui.graphics.Color = buttonColor.copy(alpha = 0.1f),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, buttonColor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon in circular background with gradient effect
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = buttonColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                Text(
                    text = buttonText,
                    color = White
                )
            }
        }
    }
}

