package com.example.bloodlink.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.enums.DonationStatus
import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.ui.theme.ErrorRed
import com.example.bloodlink.ui.theme.InfoBlue
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.WarningOrange
import com.example.bloodlink.ui.theme.White

@Composable
fun StatusBadge(
    status: RequestStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status) {
        RequestStatus.PENDING -> WarningOrange to White
        RequestStatus.ACCEPTED -> SuccessGreen to White
        RequestStatus.REFUSED -> ErrorRed to White
        RequestStatus.CANCELED -> com.example.bloodlink.ui.theme.GrayMedium to White
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = status.value,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DonationStatusBadge(
    status: DonationStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status) {
        DonationStatus.CONFIRMED -> SuccessGreen to White
        DonationStatus.CANCELED -> ErrorRed to White
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = status.value,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

