package com.example.bloodlink.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.InfoBlue
import com.example.bloodlink.ui.theme.White

@Composable
fun CheckEmailScreen(
    email: String,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            IconButton(onClick = onBackToLoginClick) {
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
                modifier = Modifier.clickable(onClick = onBackToLoginClick)
            )
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email Icon
            Surface(
                shape = RoundedCornerShape(50),
                color = InfoBlue.copy(alpha = 0.1f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = InfoBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Check Your Email",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "We've sent a password reset link to",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = email,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Alert Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = InfoBlue.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    1.dp,
                    InfoBlue.copy(alpha = 0.3f)
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
                        contentDescription = "Info",
                        tint = InfoBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "If you don't see the email, check your spam folder.",
                        fontSize = 14.sp,
                        color = InfoBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BloodLinkButton(
                text = "Back to Login",
                onClick = onBackToLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

