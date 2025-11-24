package com.example.bloodlink.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.White

@Composable
fun BloodTypeChip(
    bloodType: BloodType,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        AssistChip(
            onClick = onClick,
            label = {
                Text(
                    text = bloodType.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            },
            modifier = modifier.padding(horizontal = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (selected) BloodRed else com.example.bloodlink.ui.theme.GrayLight,
                labelColor = if (selected) White else com.example.bloodlink.ui.theme.Black
            )
        )
    } else {
        Surface(
            modifier = modifier.padding(horizontal = 4.dp),
            shape = RoundedCornerShape(8.dp),
            color = if (selected) BloodRed else com.example.bloodlink.ui.theme.GrayLight
        ) {
            Text(
                text = bloodType.value,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = if (selected) White else com.example.bloodlink.ui.theme.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

