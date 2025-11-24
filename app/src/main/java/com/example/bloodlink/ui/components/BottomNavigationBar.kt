package com.example.bloodlink.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    // Donor Navigation
    object DonorDashboard : BottomNavItem("donor_dashboard", "Dashboard", Icons.Default.Home)
    object DonorAlerts : BottomNavItem("donor_notifications", "Alerts", Icons.Default.Notifications)
    object DonorProfile : BottomNavItem("donor_profile", "Profile", Icons.Default.Person)

    // Doctor Navigation
    object DoctorDashboard : BottomNavItem("doctor_dashboard", "Dashboard", Icons.Default.Home)
    object DoctorRequests : BottomNavItem("blood_requests", "Requests", Icons.Default.List)
    object DoctorProfile : BottomNavItem("doctor_profile", "Settings", Icons.Default.Person)

    // Blood Bank Navigation
    object BloodBankDashboard : BottomNavItem("blood_bank_dashboard", "Dashboard", Icons.Default.Home)
    object BloodBankNotifications : BottomNavItem("alert_responses", "Notifications", Icons.Default.Notifications)
    object BloodBankSettings : BottomNavItem("blood_bank_settings", "Settings", Icons.Default.Settings)
}

@Composable
fun DonorBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = com.example.bloodlink.ui.theme.White,
        contentColor = GrayMedium
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == BottomNavItem.DonorDashboard.route,
            onClick = { onNavigate(BottomNavItem.DonorDashboard.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
            label = { Text("Alerts") },
            selected = currentRoute == BottomNavItem.DonorAlerts.route,
            onClick = { onNavigate(BottomNavItem.DonorAlerts.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == BottomNavItem.DonorProfile.route,
            onClick = { onNavigate(BottomNavItem.DonorProfile.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun DoctorBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = com.example.bloodlink.ui.theme.White,
        contentColor = GrayMedium
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == BottomNavItem.DoctorDashboard.route,
            onClick = { onNavigate(BottomNavItem.DoctorDashboard.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Requests") },
            label = { Text("Requests") },
            selected = currentRoute == BottomNavItem.DoctorRequests.route,
            onClick = { onNavigate(BottomNavItem.DoctorRequests.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Settings") },
            label = { Text("Profile") },
            selected = currentRoute == BottomNavItem.DoctorProfile.route,
            onClick = { onNavigate(BottomNavItem.DoctorProfile.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun BloodBankBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = com.example.bloodlink.ui.theme.White,
        contentColor = GrayMedium
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == BottomNavItem.BloodBankDashboard.route,
            onClick = { onNavigate(BottomNavItem.BloodBankDashboard.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
            label = { Text("Notifications") },
            selected = currentRoute == BottomNavItem.BloodBankNotifications.route,
            onClick = { onNavigate(BottomNavItem.BloodBankNotifications.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == BottomNavItem.BloodBankSettings.route,
            onClick = { onNavigate(BottomNavItem.BloodBankSettings.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BloodRed,
                selectedTextColor = BloodRed,
                indicatorColor = BloodRed.copy(alpha = 0.1f)
            )
        )
    }
}

