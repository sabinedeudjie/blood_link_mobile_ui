package com.example.bloodlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkOutlinedButton
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Blood Link Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                BloodLinkLogo(size = 88.dp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Blood Link Title
            Text(
                text = "Blood Link",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = BloodRed,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle
            Text(
                text = "Connecting Lives Through Blood Donation",
                fontSize = 16.sp,
                color = GrayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // Register Button (Outlined)
            BloodLinkOutlinedButton(
                text = "Register",
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Login Button (Filled)
            BloodLinkButton(
                text = "Login",
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Footer text
            Text(
                text = "Every drop counts, every life matters",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BloodRed,
                textAlign = TextAlign.Center
            )
        }
    }
}

