package com.example.bloodlink.ui.screens.donor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.getIsRead
import com.example.bloodlink.data.getMessage
import com.example.bloodlink.data.getTimestamp
import com.example.bloodlink.data.model.appl.Donation
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.DonationStatus
import com.example.bloodlink.data.model.enums.Gender.FEMALE
import com.example.bloodlink.data.model.enums.Gender.MALE
import com.example.bloodlink.data.model.metiers.Donor
import com.example.bloodlink.navigation.Screen
import com.example.bloodlink.retrofit.SharedModel
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.DonorBottomNavigation
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorDashboardScreen(
    sharedModel: SharedModel,
//    userName: String = user,//AuthState.getCurrentUser(LocalContext.current)?.username ?: "###",
//    bloodType: BloodType = BloodType.B_NEGATIVE,//(AuthState.getCurrentUser(LocalContext.current) as Donor).bloodType ?: BloodType.A_POSITIVE,
    onProfileClick: () -> Unit,
    onHealthProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onDonationHistoryClick: () -> Unit,
    onNavigate: (String) -> Unit,
    notifications: List<Map<String, Any>> = emptyList(),
    donations: List<Donation> = emptyList(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user: Donor = sharedModel.connectedUser as Donor

    // Get actual donor data from DonorProfileState
    val personalData by DonorProfileState::savedPersonalData
    val savedDonations = DonorProfileState.donationHistory

    // Use actual data or fallback to parameters/defaults
    val donorName = user.username ?: "###"
    val donorBloodType =
        user.bloodType ?: BloodType.A_POSITIVE
    val donorGender =
        user.medicalProfile?.personalInfos?.gender ?: MALE
    val donorEmail = user.username ?: "###"

    // Use saved donations or provided donations
    val allDonations: List<Donation> =
        if (savedDonations.isNotEmpty()) savedDonations.toList() else donations

    // Get last donation date: from donation history
    val lastDonationFromHistory = allDonations.firstOrNull()
    val lastDonationDate = user.lastDonationDate ?: LocalDate.now()

    val totalDonations = allDonations.size
    val livesSaved = totalDonations

    // Get eligibility status from DonorProfileState (reactive)
    val isEligible by com.example.bloodlink.ui.screens.donor.DonorProfileState::isEligible

    // Calculate next eligible date based on gender and last donation
    // Female: 4 months (120 days), Male: 3 months (90 days)
    // Add months based on gender
    val monthsToAdd = when (donorGender) {
        FEMALE -> 4
        MALE -> 3
    }
    val nextEligibleDate = lastDonationDate + Period.ofMonths(monthsToAdd)

Scaffold(
topBar = {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BloodLinkLogo(
                    size = 24.dp,
                    tint = White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Blood Link")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BloodRed,
            titleContentColor = White
        )
    )
},
bottomBar = {
    DonorBottomNavigation(
        currentRoute = Screen.DonorDashboard.route,
        onNavigate = onNavigate
    )
}
) {
    paddingValues ->
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(com.example.bloodlink.ui.theme.GrayLight)
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Section
        item {
            WelcomeSection(
                userName = donorName,
                bloodType = donorBloodType,
                isEligible = isEligible
            )
        }

        // Stats Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Blood Type",
                    value = donorBloodType.value,
                    useLogo = true,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Last Donation",
                    value = lastDonationDate?.let {
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
                    } ?: "Never",
                    icon = Icons.Default.History,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Total Donations",
                    value = totalDonations.toString(),
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Next Eligible",
                    value = nextEligibleDate.toString(),
                    icon = Icons.Default.CalendarToday,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Activity Section
        item {
            RecentActivitySection(
                donations = allDonations.take(3)
            )
        }

        // Your Impact Section
        item {
            ImpactCard(livesSaved = livesSaved)
        }
    }
}
}

@Composable
fun WelcomeSection(
    userName: String,
    bloodType: BloodType,
    isEligible: Boolean?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black
            )
            Text(
                text = "Your donation dashboard",
                fontSize = 14.sp,
                color = GrayMedium
            )
        }

    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    useLogo: Boolean = false,
    modifier: Modifier = Modifier
) {
    BloodLinkCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = GrayMedium
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (label == "Blood Type") BloodRed else com.example.bloodlink.ui.theme.Black
                )
            }
            if (useLogo) {
                BloodLinkLogo(
                    size = 24.dp,
                    tint = if (label == "Blood Type") BloodRed else com.example.bloodlink.ui.theme.Black
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = BloodRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AlertsSection(
    notifications: List<Map<String, Any>>,
    onViewAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Blood Donation Alerts",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black
            )
            TextButton(onClick = onViewAllClick) {
                Text("View All", color = BloodRed)
            }
        }
        Text(
            text = "Review and respond to blood donation requests",
            fontSize = 14.sp,
            color = GrayMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (notifications.isEmpty()) {
            BloodLinkCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = GrayMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Alerts Yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                    Text(
                        text = "Welcome to Blood Link! You'll receive notifications here when there are blood donation requests matching your blood type in your area. Make sure to complete your profile and enable notifications.",
                        fontSize = 14.sp,
                        color = GrayMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            notifications.take(3).forEach { notification ->
                DashboardNotificationCard(notification = notification)
            }
        }
    }
}

@Composable
private fun DashboardNotificationCard(notification: Map<String, Any>) {
    BloodLinkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = BloodRed,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.getMessage(),
                    fontSize = 16.sp,
                    fontWeight = if (!notification.getIsRead()) FontWeight.Bold else FontWeight.Normal,
                    color = com.example.bloodlink.ui.theme.Black
                )
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(notification.getTimestamp()),
                    fontSize = 12.sp,
                    color = GrayMedium
                )
            }
        }
    }
}

@Composable
fun RecentActivitySection(donations: List<Donation>) {
    Column {
        Text(
            text = "Recent Activity",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = com.example.bloodlink.ui.theme.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (donations.isEmpty()) {
            Text(
                text = "No recent activity",
                fontSize = 14.sp,
                color = GrayMedium
            )
        } else {
            donations.forEach { donation ->
                ActivityItem(donation = donation)
            }
        }
    }
}

@Composable
fun ActivityItem(donation: Donation) {
    BloodLinkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (donation.status == DonationStatus.CONFIRMED) SuccessGreen else com.example.bloodlink.ui.theme.ErrorRed,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = if (donation.status == DonationStatus.CONFIRMED) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Blood donation ${donation.status?.value?.lowercase() ?: "unknown"}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.bloodlink.ui.theme.Black
                )
                Text(
                    text = "${
                        donation.donationDate?.let {
                            SimpleDateFormat(
                                "MMMM dd, yyyy",
                                Locale.getDefault()
                            ).format(
                                java.util.Date.from(
                                    it.atZone(java.time.ZoneId.systemDefault()).toInstant()
                                )
                            )
                        } ?: "Unknown date"
                    } • City Hospital",
                    fontSize = 12.sp,
                    color = GrayMedium
                )
            }
        }
    }
}

@Composable
fun ImpactCard(livesSaved: Int) {
    BloodLinkCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Your Impact",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.bloodlink.ui.theme.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "You've helped save up to ",
                    fontSize = 14.sp,
                    color = com.example.bloodlink.ui.theme.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$livesSaved lives",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BloodRed
                    )
                    Text(
                        text = " through your donations",
                        fontSize = 14.sp,
                        color = com.example.bloodlink.ui.theme.Black
                    )
                }
                Text(
                    text = "Each blood donation can help save up to 3 lives",
                    fontSize = 12.sp,
                    color = GrayMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            BloodLinkLogo(
                size = 48.dp,
                tint = BloodRed
            )
        }
    }
}
