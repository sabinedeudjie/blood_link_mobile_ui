package com.example.bloodlink.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayLight
import com.example.bloodlink.ui.theme.GrayMedium

@Composable
fun BloodLinkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = GrayMedium
                )
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BloodRed,
            unfocusedBorderColor = GrayMedium,
            focusedLabelColor = BloodRed,
            unfocusedLabelColor = GrayMedium,
            errorBorderColor = com.example.bloodlink.ui.theme.ErrorRed,
            focusedContainerColor = GrayLight,
            unfocusedContainerColor = GrayLight
        ),
        isError = isError,
        supportingText = if (isError) {
            { Text(errorMessage) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        enabled = enabled,
        visualTransformation = visualTransformation
    )
}

