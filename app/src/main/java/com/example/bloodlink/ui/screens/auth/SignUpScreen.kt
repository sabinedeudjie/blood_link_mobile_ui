package com.example.bloodlink.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.model.dtos.requests.RegisterRequest
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.Gender
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.data.model.metiers.User
import com.example.bloodlink.retrofit.RetrofitInstance
import com.example.bloodlink.retrofit.TokenManager
import com.example.bloodlink.ui.components.BloodLinkButton
import com.example.bloodlink.ui.components.BloodLinkDropdown
import com.example.bloodlink.ui.components.BloodLinkLogo
import com.example.bloodlink.ui.components.BloodLinkTextField
import com.example.bloodlink.ui.components.DatePickerField
import com.example.bloodlink.ui.screens.bloodbank.BloodBankDataForm
import com.example.bloodlink.ui.screens.bloodbank.BloodBankProfileState
import com.example.bloodlink.ui.screens.doctor.DoctorDataForm
import com.example.bloodlink.ui.screens.doctor.DoctorProfileState
import com.example.bloodlink.ui.screens.donor.DonorDataForm
import com.example.bloodlink.ui.screens.donor.DonorProfileState
import com.example.bloodlink.ui.screens.donor.PersonalDataForm
import com.example.bloodlink.ui.theme.Black
import com.example.bloodlink.ui.theme.BloodRed
import com.example.bloodlink.ui.theme.GrayMedium
import com.example.bloodlink.ui.theme.White
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import java.util.regex.Pattern

// Validation functions
fun isValidEmail(email: String): Boolean {
    val emailPattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$",
        Pattern.CASE_INSENSITIVE
    )
    return emailPattern.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    // Password must be at least 8 characters long
    // and contain at least one letter and one number
    if (password.length < 8) return false
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    return hasLetter && hasDigit
}



@Composable
fun SignUpScreen(
    role: UserRole = UserRole.DONOR,
    onSignUpClick: (Map<String, String>) -> Unit, // Changed to accept a map of all fields
    onSignUpClick2: (User?) -> Unit, // Changed to accept AuthenticationResponse
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    // Common fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Validation error messages
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Donor-specific fields
    var dateOfBirth by remember { mutableStateOf<Date?>(null) }
    var lastDonationDate by remember { mutableStateOf<Date?>(null) }
    var gender by remember { mutableStateOf("") } // "Male" or "Female"
    var bloodType by remember { mutableStateOf<BloodType?>(null) }
    var address by remember { mutableStateOf("") }

    // Doctor-specific fields
    var hospitalName by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var medicalLicenseNumber by remember { mutableStateOf("") }

    // Blood Bank-specific fields
    var bloodBankName by remember { mutableStateOf("") }
    var bloodBankAddress by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }

    val bloodTypeOptions = BloodType.entries.map { it.value }
    val specializationOptions = listOf(
        "General Medicine",
        "Cardiology",
        "Hematology",
        "Emergency Medicine",
        "Internal Medicine",
        "Surgery",
        "Pediatrics",
        "Other"
    )

    val title = when (role) {
        UserRole.DONOR -> "Donor Registration"
        UserRole.DOCTOR -> "Doctor Registration"
        UserRole.BLOOD_BANK -> "Blood Bank Registration"
    }
        val context = LocalContext.current

        val scope = rememberCoroutineScope()

    /*Nouveau sign up to screen 1*/

    suspend fun getCurrentUserDetails(context: Context): User?{
        val tokenManager = TokenManager(context)
        return try {
            Log.d("AuthState", "Fetching current user details")
            val user = RetrofitInstance.getUserApi(context).getMe()
            user
        } catch (e : Exception) {
            Log.e("AuthState", "Failed to fetch current user details: ${e.message}")
            e.printStackTrace()
            null
        }
    }

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
                .padding(20.dp),
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
                text = "Back",
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
            // Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                BloodLinkLogo(
                    size = 32.dp,
                    tint = BloodRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Blood Link",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BloodRed
                )
            }

            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Join the Blood Link community and start saving lives",
                fontSize = 14.sp,
                color = GrayMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Registration Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (role) {
                    ///////////////////Specific fields for each role
                    UserRole.DONOR -> {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BloodLinkTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = "First Name *",
                                placeholder = "Enter first name",
                                modifier = Modifier.weight(1f)
                            )
                            BloodLinkTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = "Last Name *",
                                placeholder = "Enter last name",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        BloodLinkTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                // Validate email format
                                emailError = if (it.isNotBlank() && !isValidEmail(it)) {
                                    "Please enter a valid email address"
                                } else {
                                    ""
                                }
                            },
                            label = "Email *",
                            placeholder = "Enter your email",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Email,
                            isError = emailError.isNotBlank(),
                            errorMessage = emailError
                        )

                        BloodLinkTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Phone Number *",
                            placeholder = "Enter phone number",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Phone
                        )


                        BloodLinkDropdown(
                            value = bloodType?.name,
                            onValueChange = { bloodType = BloodType.fromString(it) },
                            label = "Blood Type *",
                            options = bloodTypeOptions,
                            placeholder = "Select blood type",
                            modifier = Modifier.fillMaxWidth()
                        )

                        DatePickerField(
                            value = lastDonationDate,
                            onValueChange = { lastDonationDate = it },
                            label = "Last Donation Date",
                            placeholder = "Select date (Optional)",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    UserRole.DOCTOR -> {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BloodLinkTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = "First Name *",
                                placeholder = "Enter first name",
                                modifier = Modifier.weight(1f)
                            )
                            BloodLinkTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = "Last Name *",
                                placeholder = "Enter last name",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        BloodLinkTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                // Validate email format
                                emailError = if (it.isNotBlank() && !isValidEmail(it)) {
                                    "Please enter a valid email address"
                                } else {
                                    ""
                                }
                            },
                            label = "Email *",
                            placeholder = "Enter your email",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Email,
                            isError = emailError.isNotBlank(),
                            errorMessage = emailError
                        )

                        BloodLinkTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Phone Number *",
                            placeholder = "Enter phone number",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Phone
                        )

                        BloodLinkTextField(
                            value = hospitalName,
                            onValueChange = { hospitalName = it },
                            label = "Hospital Name *",
                            placeholder = "Enter hospital name",
                            modifier = Modifier.fillMaxWidth()
                        )

                        BloodLinkDropdown(
                            value = bloodType?.name,
                            onValueChange = { specialization = it },
                            label = "Specialization *",
                            options = specializationOptions,
                            placeholder = "Select specialization",
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                    UserRole.BLOOD_BANK -> {

                        BloodLinkTextField(
                            value = bloodBankName,
                            onValueChange = { bloodBankName = it },
                            label = "Blood Bank Name *",
                            placeholder = "Enter blood bank name",
                            modifier = Modifier.fillMaxWidth()
                        )

                        BloodLinkTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                // Validate email format
                                emailError = if (it.isNotBlank() && !isValidEmail(it)) {
                                    "Please enter a valid email address"
                                } else {
                                    ""
                                }
                            },
                            label = "Email *",
                            placeholder = "Enter your email",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Email,
                            isError = emailError.isNotBlank(),
                            errorMessage = emailError
                        )

                        BloodLinkTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Phone Number *",
                            placeholder = "Enter phone number",
                            modifier = Modifier.fillMaxWidth(),
                            keyboardType = KeyboardType.Phone
                        )

                        BloodLinkTextField(
                            value = bloodBankAddress,
                            onValueChange = { bloodBankAddress = it },
                            label = "Address *",
                            placeholder = "Enter blood bank address",
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false
                        )

                    }
                }

                // Password fields (common for all)

                BloodLinkTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        // Validate password strength
                        passwordError = when {
                            it.isBlank() -> ""
                            it.length < 8 -> "Password must be at least 8 characters long"
                            !it.any { char -> char.isLetter() } -> "Password must contain at least one letter"
                            !it.any { char -> char.isDigit() } -> "Password must contain at least one number"
                            else -> ""
                        }
                        // Clear confirm password error if passwords match
                        if (confirmPassword.isNotBlank() && it == confirmPassword) {
                            confirmPasswordError = ""
                        }
                    },
                    label = "Password *",
                    placeholder = "Enter your password (min 8 characters, letters and numbers)",
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError.isNotBlank(),
                    errorMessage = passwordError
                )

                BloodLinkTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        // Validate password match
                        confirmPasswordError = if (it.isNotBlank() && it != password) {
                            "Passwords do not match"
                        } else {
                            ""
                        }
                    },
                    label = "Confirm Password *",
                    placeholder = "Confirm your password",
                    modifier = Modifier.fillMaxWidth(),
                    isError = confirmPasswordError.isNotBlank(),
                    errorMessage = confirmPasswordError,
                    visualTransformation = PasswordVisualTransformation()
                )

                // Terms of Service checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { agreeToTerms = !agreeToTerms },
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = agreeToTerms,
                        onCheckedChange = { agreeToTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = BloodRed),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "I agree to the ",
                                fontSize = 14.sp,
                                color = Black,
                                lineHeight = 20.sp
                            )
                            Text(
                                text = "Terms of Service",
                                fontSize = 14.sp,
                                color = BloodRed,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                modifier = Modifier.clickable { /* TODO: Navigate to terms */ }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "and ",
                                fontSize = 14.sp,
                                color = Black,
                                lineHeight = 20.sp
                            )
                            Text(
                                text = "Privacy Policy",
                                fontSize = 14.sp,
                                color = BloodRed,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                modifier = Modifier.clickable { /* TODO: Navigate to privacy */ }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Validation function
                val isValid = when (role) {
                    UserRole.DONOR -> {
                        firstName.isNotBlank() && lastName.isNotBlank() &&
                                email.isNotBlank() && isValidEmail(email) && emailError.isBlank() &&
                                phone.isNotBlank() &&
                                bloodType != null &&
                                password.isNotBlank() && isValidPassword(password) && passwordError.isBlank() &&
                                password == confirmPassword && confirmPasswordError.isBlank() &&
                                agreeToTerms
                    }

                    UserRole.DOCTOR -> {
                        firstName.isNotBlank() && lastName.isNotBlank() &&
                                email.isNotBlank() && isValidEmail(email) && emailError.isBlank() &&
                                phone.isNotBlank() &&
                                hospitalName.isNotBlank() && specialization.isNotBlank() &&
                                password.isNotBlank() && isValidPassword(password) && passwordError.isBlank() &&
                                password == confirmPassword && confirmPasswordError.isBlank() &&
                                agreeToTerms
                    }

                    UserRole.BLOOD_BANK -> {
                        bloodBankName.isNotBlank() &&
                                email.isNotBlank() && isValidEmail(email) && emailError.isBlank() &&
                                phone.isNotBlank() && bloodBankAddress.isNotBlank() &&
                                password.isNotBlank() && isValidPassword(password) && passwordError.isBlank() &&
                                password == confirmPassword && confirmPasswordError.isBlank() &&
                                agreeToTerms
                    }

                    else -> false
                }

                BloodLinkButton(
                    text = "Register",
                    onClick = {
                        scope.launch {
                            if (isValid) {
                                isLoading = true
                                val fields = mutableMapOf<String, String>()
                                var request: RegisterRequest? = null
                                when (role) {
                                    UserRole.DONOR -> {
                                        fields["firstName"] = firstName
                                        fields["lastName"] = lastName
                                        fields["email"] = email
                                        fields["phone"] = phone
                                        // Note: dateOfBirth and lastDonationDate are now Date objects, not strings
                                        fields["bloodType"] = bloodType?.name ?: ""
                                        fields["password"] = password

                                        // Save gender to DonorProfileState for use in health questions

                                        // Convert Date to LocalDate for PersonalDataForm
                                        val birthdateLocalDate = dateOfBirth?.let { date ->
                                            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                                        }

                                        // Convert gender string to Gender enum
                                        val genderEnum = when (gender.lowercase()) {
                                            "male" -> Gender.MALE
                                            "female" -> Gender.FEMALE
                                            else -> null
                                        }

                                        // Convert bloodType string to BloodType enum
                                        val bloodTypeEnum = bloodType

                                        // Create DonorDataForm with contact information
                                        val contactData = DonorDataForm(
                                            firstName = firstName,
                                            lastName = lastName,
                                            email = email,
                                            phone = phone,
                                            bloodType = bloodTypeEnum
                                        )

                                        // Create PersonalDataForm with only PersonalInfos fields
                                        val personalData = PersonalDataForm(
                                            birthdate = birthdateLocalDate,
                                            gender = genderEnum,
                                            weight = 0f, // Will be set later in health profile
                                            emergencyContact = "" // Will be set later in personal info form
                                        )

                                        // Save to DonorProfileState
                                        DonorProfileState.savedPersonalData = personalData
                                        DonorProfileState.password = password

                                        request = RegisterRequest(
                                            name = firstName,
                                            surname = lastName,
                                            email = email,
                                            password = password,
                                            phoneNumber = phone,
                                            userRole = UserRole.DONOR,
                                            LastDonationDate = lastDonationDate?.toInstant()?.atZone(ZoneId.systemDefault())
                                                ?.toLocalDate()
                                                .toString(),// String.format("yyyy-MM-dd", lastDonationDate),
                                            //LastDonationDate = String.format("yyyy-MM-dd") "${lastDonationDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.year}-${lastDonationDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.monthValue}-${lastDonationDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.dayOfMonth}",//) as LocalDate?,
                                            bloodType = bloodType
                                        )
                                    }

                                    UserRole.DOCTOR -> {
                                        fields["firstName"] = firstName
                                        fields["lastName"] = lastName
                                        fields["email"] = email
                                        fields["phone"] = phone
                                        fields["hospitalName"] = hospitalName
                                        fields["specialization"] = specialization
                                        fields["password"] = password

                                        // Save doctor data to DoctorProfileState
                                        val doctorData = DoctorDataForm(
                                            firstName = firstName,
                                            lastName = lastName,
                                            email = email,
                                            phone = phone,
                                            hospitalName = hospitalName,
                                            specialization = specialization,
                                        )
                                        DoctorProfileState.savedDoctorData = doctorData
                                        DoctorProfileState.password = password

                                        request = RegisterRequest(
                                            name = firstName,
                                            surname = lastName,
                                            email = email,
                                            password = password,
                                            phoneNumber = phone,
                                            userRole = UserRole.DOCTOR,
                                            hospitalName = hospitalName,
                                            speciality = specialization,
                                        )
                                    }

                                    UserRole.BLOOD_BANK -> {
                                        fields["bloodBankName"] = bloodBankName
                                        fields["email"] = email
                                        fields["phone"] = phone
                                        fields["address"] = bloodBankAddress
                                        fields["password"] = password

                                        // Save blood bank data to BloodBankProfileState
                                        val bloodBankData = BloodBankDataForm(
                                            bloodBankName = bloodBankName,
                                            email = email,
                                            phone = phone,
                                            address = bloodBankAddress,
                                        )
                                        BloodBankProfileState.savedBloodBankData = bloodBankData
                                        BloodBankProfileState.password = password

                                        // Create RegisterRequest with bloodBankName
                                        request = RegisterRequest(
                                            name = firstName,
                                            surname = lastName,
                                            email = email,
                                            password = password,
                                            phoneNumber = phone,
                                            userRole = UserRole.BLOOD_BANK,
                                            bloodBankName = bloodBankName,
                                            address = address,
                                            licenseNumber = UUID.fromString(licenseNumber)
                                        )
                                    }
                                }

                                // Register user in AuthState
                                isLoading = true
                                scope.launch {
                                    try {
                                        val authResponse = AuthState.registerUser(request, context = context)
                                        isLoading = false
                                        if (authResponse != null) {
                                            // Registration successful, pass the response to callback
                                            val user = AuthState.getCurrentUser(context)
                                            onSignUpClick2(user)
                                            onSignUpClick(fields)
                                        } else {
                                            // Registration failed
                                            // TODO: Show error message to user
                                        }
                                    } catch (e: Exception) {
                                        isLoading = false
                                        e.printStackTrace()
                                        // TODO: Show error message to user
                                    }
                                }
                            }
                        }
                    },
                    enabled = !isLoading && isValid
                )
            }
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
                text = "Login",
                fontSize = 14.sp,
                color = BloodRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
