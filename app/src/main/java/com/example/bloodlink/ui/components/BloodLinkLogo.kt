package com.example.bloodlink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bloodlink.R

@Composable
fun BloodLinkLogo(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color? = null
) {
    // Try to load the logo, fallback to icon if resource doesn't exist
    // Note: If logo.png doesn't exist, this will cause a compilation error
    // Make sure logo.png is in res/drawable/
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Blood Link Logo",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}

