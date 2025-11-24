package com.example.bloodlink.ui.screens.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.SharedRequestsState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.RequestStatus
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkCard
import com.example.bloodlink.ui.components.BloodLinkDropdown
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.SuccessGreen
import com.example.bloodlink.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBloodRequestScreen(
    onSaveClick: (BloodType, Int, String, List<String>) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bloodType by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showBloodBankSelection by remember { mutableStateOf(false) }
    var selectedBloodBankEmails by remember { mutableStateOf<Set<String>>(emptySet()) }

    val bloodTypeOptions = BloodType.entries.map { it.value }
    
    // Get all registered blood banks
    val availableBloodBanks = remember {
        UserDataStore.getAllBloodBanks()
    }

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
                        Text("New Blood Request")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BloodRed,
                    titleContentColor = White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(com.example.bloodlink.ui.theme.GrayLight)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Request Blood Units",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = com.example.bloodlink.ui.theme.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Fill in the details below to create a new blood request",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BloodLinkCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BloodLinkDropdown(
                        value = bloodType,
                        onValueChange = { bloodType = it },
                        label = "Blood Type *",
                        options = bloodTypeOptions,
                        placeholder = "Select blood type",
                        modifier = Modifier.fillMaxWidth()
                    )

                    BloodLinkTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = "Units Needed *",
                        placeholder = "Enter number of units",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number
                    )

                    BloodLinkTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Reason of request",
                        placeholder = "Additional information (optional)",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Blood Bank Selection
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Select Blood Banks *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = com.example.bloodlink.ui.theme.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Selected blood banks display
                        if (selectedBloodBankEmails.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                selectedBloodBankEmails.forEach { email ->
                                    val bloodBank = availableBloodBanks.firstOrNull { it.email == email }
                                    if (bloodBank != null) {
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = SuccessGreen.copy(alpha = 0.1f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = bloodBank.bloodBankName,
                                                    fontSize = 12.sp,
                                                    color = SuccessGreen,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                IconButton(
                                                    onClick = {
                                                        selectedBloodBankEmails = selectedBloodBankEmails - email
                                                    },
                                                    modifier = Modifier.size(16.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove",
                                                        tint = SuccessGreen,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Button to open blood bank selection dialog
                        OutlinedButton(
                            onClick = { showBloodBankSelection = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = BloodRed
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BloodRed)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedBloodBankEmails.isEmpty()) 
                                    "Select Blood Banks" 
                                else 
                                    "Add More Blood Banks",
                                fontSize = 14.sp
                            )
                        }
                        
                        if (selectedBloodBankEmails.isEmpty()) {
                            Text(
                                text = "Please select at least one blood bank",
                                fontSize = 12.sp,
                                color = com.example.bloodlink.ui.theme.ErrorRed,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val isValid = bloodType.isNotBlank() && 
                                  quantity.isNotBlank() && 
                                  quantity.toIntOrNull() != null && 
                                  quantity.toIntOrNull()!! > 0 &&
                                  selectedBloodBankEmails.isNotEmpty()

                    BloodLinkButton(
                        text = "Create Request",
                        onClick = {
                            if (isValid) {
                                isLoading = true
                                val selectedBloodType = BloodType.fromString(bloodType)
                                if (selectedBloodType != null) {
                                    // Generate unique request ID based on total requests (doctor + shared)
                                    val requestId = DoctorProfileState.getAllRequestsData().size + SharedRequestsState.getAllRequestsData().size + 1
                                    
                                    // Convert email list to ID list
                                    val selectedBloodBankIds = selectedBloodBankEmails.map { email ->
                                        availableBloodBanks.firstOrNull { it.email == email }?.email?.hashCode() ?: 0
                                    }.filter { it != 0 }
                                    
                                    // Create and save the request using helper
                                    // Since BloodRequest is not a data class, we store data in a Map
                                    val requestIdUUID = java.util.UUID.randomUUID()
                                    val requestData = mapOf<String, Any>(
                                        "id" to requestIdUUID,
                                        "recipientType" to selectedBloodType,
                                        "quantityNeeded" to quantity.toLong(),
                                        "status" to RequestStatus.PENDING,
                                        "selectedBloodBankEmails" to selectedBloodBankEmails.toList(),
                                        "requestIdInt" to requestId
                                    )
                                    
                                    // Store in DoctorProfileState (using Map for now)
                                    DoctorProfileState.addBloodRequestData(requestData)
                                    
                                    // Store in SharedRequestsState (using Map for now)
                                    SharedRequestsState.addBloodRequestData(requestData)
                                    
                                    // Reset loading state
                                    isLoading = false
                                    
                                    // Call the callback to navigate back
                                    onSaveClick(
                                        selectedBloodType,
                                        quantity.toInt(),
                                        notes,
                                        selectedBloodBankEmails.toList()
                                    )
                                }
                            }
                        },
                        enabled = !isLoading && isValid,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
    
    // Blood Bank Selection Dialog
    if (showBloodBankSelection) {
        AlertDialog(
            onDismissRequest = { showBloodBankSelection = false },
            title = {
                Text(
                    text = "Select Blood Banks",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                if (availableBloodBanks.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            tint = GrayMedium,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Blood Banks Available",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "There are no blood banks registered yet.",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableBloodBanks) { bloodBank ->
                            val isSelected = selectedBloodBankEmails.contains(bloodBank.email)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedBloodBankEmails = if (isSelected) {
                                            selectedBloodBankEmails - bloodBank.email
                                        } else {
                                            selectedBloodBankEmails + bloodBank.email
                                        }
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) 
                                    SuccessGreen.copy(alpha = 0.1f) 
                                else 
                                    com.example.bloodlink.ui.theme.White,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp, 
                                    if (isSelected) SuccessGreen else GrayMedium.copy(alpha = 0.3f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = bloodBank.bloodBankName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = com.example.bloodlink.ui.theme.Black
                                        )
                                        Text(
                                            text = bloodBank.address,
                                            fontSize = 12.sp,
                                            color = GrayMedium
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showBloodBankSelection = false }
                ) {
                    Text("Done", color = BloodRed)
                }
            }
        )
    }
}

