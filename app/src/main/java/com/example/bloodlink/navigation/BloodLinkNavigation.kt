package com.example.bloodlink.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bloodlink.data.AuthState
import com.example.bloodlink.data.SharedRequestsState
import com.example.bloodlink.data.UserDataStore
import com.example.bloodlink.data.getRequestIdInt
import com.example.bloodlink.data.model.appl.StockByType
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.data.model.enums.UserRole.BLOOD_BANK
import com.example.bloodlink.data.model.enums.UserRole.DOCTOR
import com.example.bloodlink.data.model.enums.UserRole.DONOR
import com.example.bloodlink.retrofit.SharedModel
import com.example.bloodlink.ui.screens.HelloWorldScreen
import com.example.bloodlink.ui.screens.HomeScreen
import com.example.bloodlink.ui.screens.auth.CheckEmailScreen
import com.example.bloodlink.ui.screens.auth.ForgotPasswordScreen
import com.example.bloodlink.ui.screens.auth.LoginScreen
import com.example.bloodlink.ui.screens.auth.RoleSelectionScreen
import com.example.bloodlink.ui.screens.auth.SignUpScreen
import com.example.bloodlink.ui.screens.bloodbank.AffiliatedDonorsScreen
import com.example.bloodlink.ui.screens.bloodbank.AlertResponsesScreen
import com.example.bloodlink.ui.screens.bloodbank.BloodBankDashboardScreen
import com.example.bloodlink.ui.screens.bloodbank.BloodBankProfileScreen
import com.example.bloodlink.ui.screens.bloodbank.BloodBankProfileState
import com.example.bloodlink.ui.screens.bloodbank.BloodBankSettingsScreen
import com.example.bloodlink.ui.screens.bloodbank.CreateCustomAlertScreen
import com.example.bloodlink.ui.screens.bloodbank.EditBloodBankProfileScreen
import com.example.bloodlink.ui.screens.bloodbank.ManageRequestsScreen
import com.example.bloodlink.ui.screens.bloodbank.SendAlertScreen
import com.example.bloodlink.ui.screens.bloodbank.StockManagementScreen
import com.example.bloodlink.ui.screens.doctor.BloodRequestsScreen
import com.example.bloodlink.ui.screens.doctor.CreateBloodRequestScreen
import com.example.bloodlink.ui.screens.doctor.DoctorDashboardScreen
import com.example.bloodlink.ui.screens.doctor.DoctorProfileScreen
import com.example.bloodlink.ui.screens.doctor.DoctorProfileState
import com.example.bloodlink.ui.screens.doctor.EditDoctorProfileScreen
import com.example.bloodlink.ui.screens.donor.AddBloodBankScreen
import com.example.bloodlink.ui.screens.donor.ChangePasswordScreen
import com.example.bloodlink.ui.screens.donor.DonorDashboardScreen
import com.example.bloodlink.ui.screens.donor.DonorNotificationsScreen
import com.example.bloodlink.ui.screens.donor.DonorProfileScreen
import com.example.bloodlink.ui.screens.donor.DonorProfileState
import com.example.bloodlink.ui.screens.donor.HealthProfileScreen
import com.example.bloodlink.ui.screens.donor.HealthQuestionFormScreen
import com.example.bloodlink.ui.screens.donor.PersonalDataFormScreen
import com.example.bloodlink.ui.screens.donor.VitalSignsFormScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun BloodLinkNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterClick = {
                    navController.navigate(Screen.RoleSelection.route)
                }
            )
        }

        composable(Screen.HelloWorld.route) {
            val sharedModel: SharedModel = viewModel()
            val connectedUser = sharedModel.connectedUser
            
            HelloWorldScreen(
                userEmail = connectedUser?.username,
                userRole = connectedUser?.let { 
                    when (it) {
                        is com.example.bloodlink.data.model.metiers.Donor -> "DONOR"
                        is com.example.bloodlink.data.model.metiers.Doctor -> "DOCTOR"
                        is com.example.bloodlink.data.model.metiers.BloodBank -> "BLOOD_BANK"
                        else -> "UNKNOWN"
                    }
                },
                onLogoutClick = {
                    scope.launch {
                        AuthState.logout()
                        sharedModel.connectedUser = null
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.SignUp.route){
            val sharedModel: SharedModel = viewModel()

            SignUpScreen(
                role = UserRole.DONOR,
                onSignUpClick = {fields -> navController.navigate(Screen.RoleSelection.route)
                },
                onSignUpClick2 = {authResponse ->
                    Log.d("SignUp", "=== onSignUpClick2 CALLED ===")
                    Log.d("SignUp", "authResponse: $authResponse")
                    
                    if(authResponse == null) {
                        Log.e("SignUp", "Registration failed: authResponse is null")
                        return@SignUpScreen
                    }
                    scope.launch {
                        try {
                            Log.d("SignUp", "=== STARTING POST-REGISTRATION ===")
                            Log.d("SignUp", "Registration Successful: ${authResponse.email}, role : ${authResponse.role}")

                          //  delay(200)
                            val fullUser = AuthState.getCurrentUserDetails(context)

                            if (fullUser == null) {
                                Log.e("SignUp", "Failed to fetch user details after registration")
                                withContext(Dispatchers.Main){
                                    Toast.makeText(
                                        context,
                                        "Registration successful but failed to load profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            }
                            Log.d("SignUp", "User details fetched: ${fullUser::class.simpleName}")
                            Log.d("SignUp", "User email: ${fullUser.username}")

                            sharedModel.connectedUser = fullUser

                            if(sharedModel.connectedUser == null){
                                Log.e("SignUp", "Failed to ser connectedUser in shareModel!")
                                withContext(Dispatchers.Main){
                                    Toast.makeText(
                                        context,
                                        "Error setting up user session",
                                        Toast.LENGTH_SHORT
                                    )
                                }
                                return@launch
                            }
                            Log.d("SignUp", "ShareModel connectedUser set successfully")

                            withContext(Dispatchers.Main){
                                when(authResponse.role){
                                    UserRole.DONOR -> {
                                        DonorProfileState.savedPersonalData = null
                                        DonorProfileState.savedVitalSigns = null
                                        DonorProfileState.savedHealthQuestions = null
                                        DonorProfileState.bloodBankAffiliationEmails.clear()
                                        DonorProfileState.isEligible = null
                                        DonorProfileState.donationHistory.clear()

                                        Log.d("SignUp", "Navigating to DonorDashboard")

                                        navController.navigate(Screen.DonorDashboard.route){
                                            popUpTo(Screen.Home.route) {inclusive = true }
                                        }
                                    }
                                    UserRole.DOCTOR -> {
                                        Log.d("SignUp", "Navigating to DoctorDashboard")
                                        navController.navigate(Screen.DoctorDashboard.route){
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    }
                                    UserRole.BLOOD_BANK -> {
                                        Log.d("SignUp", "Navigating to BloodBankashboard")
                                        navController.navigate(Screen.BloodBankDashboard.route){
                                            popUpTo(Screen.Home.route) {inclusive = true }
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("SignUp", "Sign-up navigation failed: ${e.message}", e)
                            e.printStackTrace()
                            withContext(Dispatchers.Main){
                                Toast.makeText(
                                    context,
                                    "Error during registration: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.Login.route) {inclusive = true}
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Login.route) {
            val sharedModel: SharedModel = viewModel()
            var loginError by remember { mutableStateOf<String?>(null) }

            LoginScreen(
                onLoginClick = { email, password ->
                    scope.launch {
                        loginError = null
                        Log.d("Login", "=== STARTING LOGIN ===")
                        
                        try {
                            val authResult = AuthState.login(email, password, context)
                            
                            if (authResult.data != null) {
                                Log.d("Login", "Login successful, navigating to HelloWorld...")
                                
                                // Try to get full user details from backend (optional)
                                val fullUser = AuthState.getCurrentUserDetails(context)
                                
                                if (fullUser != null) {
                                    Log.d("Login", "User details fetched: ${fullUser::class.simpleName}")
                                    sharedModel.connectedUser = fullUser
                                } else {
                                    Log.w("Login", "Could not fetch user details, but continuing to HelloWorld")
                                }
                                
                                // Navigate to HelloWorld screen regardless
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.HelloWorld.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                            } else {
                                loginError = authResult.error ?: "Impossible de vous connecter. Vérifiez vos identifiants."
                            }
                        } catch (e: Exception) {
                            Log.e("Login", "Login failed with exception: ${e.message}", e)
                            loginError = "Erreur lors de la connexion : ${e.message ?: "essayer à nouveau"}"
                        }
                    }
                },
                loginError = loginError,
                onSignUpClick = {
                    navController.navigate(Screen.RoleSelection.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onSendResetLinkClick = { email ->
                    // TODO: Send reset link to backend
                    val encodedEmail = URLEncoder.encode(email, "UTF-8")
                    navController.navigate(Screen.CheckEmail.createRoute(encodedEmail))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.CheckEmail.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedEmail = backStackEntry.arguments?.getString("email") ?: ""
            val email = try {
                URLDecoder.decode(encodedEmail, "UTF-8")
            } catch (e: Exception) {
                encodedEmail
            }
            CheckEmailScreen(
                email = email,
                onBackToLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            var sharedModel: SharedModel = viewModel()
            SignUpScreen(
                role = DONOR,
                onSignUpClick = { fields ->
                    // TODO: Handle sign up logic
                    navController.navigate(Screen.RoleSelection.route)
                },
                /*onSignUpClick2 = { user ->
                    sharedModel.connectedUser = user
                    navController.navigate(Screen.DonorDashboard.route)
                },*/

                onSignUpClick2 = { authResponse ->
                    Log.d("SignUp", "=== onSignUpClick2 CALLED (route 2) ===")
                    Log.d("SignUp", "authResponse: $authResponse")
                    
                    if (authResponse == null) {
                        Log.e("SignUp", "Registration failed: authResponse is null")
                        return@SignUpScreen
                    }

                    scope.launch {
                        try {
                            Log.d("SignUp", "=== STARTING POST-REGISTRATION (route 2) ===")
                            Log.d("SignUp", "Registration response received: ${authResponse.email}, role: ${authResponse.role}")

                            // Get full user details after registration
                            val fullUser = AuthState.getCurrentUserDetails(context)

                            if (fullUser == null) {
                                Log.e("SignUp", "Failed to fetch user details after registration")
                                // Show error to user
                                return@launch
                            }

                            Log.d("SignUp", "Fetched user type: ${fullUser::class.simpleName}")

                            // Set the connected user in shared model
                            sharedModel.connectedUser = fullUser

                            // Navigate based on user role
                            withContext(Dispatchers.Main) {
                                when (authResponse.role) {
                                    UserRole.DONOR -> {
                                        navController.navigate(Screen.DonorDashboard.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    }
                                    UserRole.DOCTOR -> {
                                        navController.navigate(Screen.DoctorDashboard.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    }
                                    UserRole.BLOOD_BANK -> {
                                        navController.navigate(Screen.BloodBankDashboard.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            Log.e("SignUp", "Sign-up flow failed", e)
                            e.printStackTrace()
                            // Show error to user
                        }
                    }
                },

                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    // Navigate to role-specific registration
                    when (role) {
                        DONOR -> navController.navigate(Screen.SignUpDonor.route)
                        DOCTOR -> navController.navigate(Screen.SignUpDoctor.route)
                        BLOOD_BANK -> navController.navigate(Screen.SignUpBloodBank.route)
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.SignUpDonor.route) {
            val sharedModel: SharedModel = viewModel()
            SignUpScreen(
                role = DONOR,
                onSignUpClick = { fields ->
                    // Navigation handled by onSignUpClick2
                },
                onSignUpClick2 = { authResponse ->
                    Log.d("SignUp", "=== onSignUpClick2 CALLED (SignUpDonor route) ===")
                    Log.d("SignUp", "authResponse: $authResponse")
                    
                    if (authResponse == null) {
                        Log.e("SignUp", "Registration failed: authResponse is null")
                        return@SignUpScreen
                    }

                    scope.launch {
                        try {
                            Log.d("SignUp", "=== STARTING POST-REGISTRATION (SignUpDonor) ===")
                            Log.d("SignUp", "Registration Successful: ${authResponse.email}, role: ${authResponse.role}")

                            // Try to get full user details after registration (optional)
                            val fullUser = AuthState.getCurrentUserDetails(context)

                            if (fullUser != null) {
                                Log.d("SignUp", "User details fetched: ${fullUser::class.simpleName}")
                                sharedModel.connectedUser = fullUser
                            } else {
                                Log.w("SignUp", "Could not fetch user details, but continuing to HelloWorld")
                            }

                            // Navigate to HelloWorld regardless
                            withContext(Dispatchers.Main) {
                                navController.navigate(Screen.HelloWorld.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("SignUp", "Error during post-registration: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SignUpDoctor.route) {
            val sharedModel: SharedModel = viewModel()
            SignUpScreen(
                role = DOCTOR,
                onSignUpClick = { fields ->
                    // Navigation handled by onSignUpClick2
                },
                onSignUpClick2 = { authResponse ->
                    if (authResponse == null) return@SignUpScreen
                    scope.launch {
                        try {
                            val fullUser = AuthState.getCurrentUserDetails(context)
                            if (fullUser != null) {
                                sharedModel.connectedUser = fullUser
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.HelloWorld.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SignUpBloodBank.route) {
            val sharedModel: SharedModel = viewModel()
            SignUpScreen(
                role = BLOOD_BANK,
                onSignUpClick = { fields ->
                    // Navigation handled by onSignUpClick2
                },
                onSignUpClick2 = { authResponse ->
                    if (authResponse == null) return@SignUpScreen
                    scope.launch {
                        try {
                            val fullUser = AuthState.getCurrentUserDetails(context)
                            if (fullUser != null) {
                                sharedModel.connectedUser = fullUser
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.HelloWorld.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.DonorDashboard.route) {
            val sharedModel: SharedModel = viewModel()
            DonorDashboardScreen(
                onProfileClick = {
                    navController.navigate(Screen.DonorProfile.route)
                },
                onHealthProfileClick = {
                    navController.navigate(Screen.HealthProfile.route)
                },
                onNotificationsClick = {
                    navController.navigate(Screen.DonorNotifications.route)
                },
                onDonationHistoryClick = {
                    // navController.navigate(Screen.DonationHistory.route)
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.DonorDashboard.route) {
                        // If already on dashboard, do nothing
                        if (currentRoute != Screen.DonorDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.DonorDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DonorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                sharedModel = sharedModel
            )
        }

        composable(Screen.DonorNotifications.route) {
            DonorNotificationsScreen(
                notifications = emptyList(), // TODO: Load from ViewModel
                onNavigate = { route ->
                    if (route == Screen.DonorDashboard.route) {
                        navController.navigate(route) {
                            popUpTo(Screen.DonorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DonorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.DonorProfile.route) {
            DonorProfileScreen(
                donor = null, // TODO: Load from ViewModel
                onNavigate = { route ->
                    if (route == Screen.DonorDashboard.route) {
                        navController.navigate(route) {
                            popUpTo(Screen.DonorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DonorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onEditProfileClick = {
                    navController.navigate(Screen.PersonalDataForm.route)
                },
                onHealthProfileClick = {
                    navController.navigate(Screen.HealthProfile.route)
                },
                onAddBloodBankClick = {
                    navController.navigate(Screen.AddBloodBank.route)
                },
                onChangePasswordClick = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onLogoutClick = {
                    // Clear user session
                    AuthState.logout()
                    // Navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.HealthProfile.route) {
            HealthProfileScreen(
                healthProfile = null, // TODO: Load from ViewModel
                onPersonalDataClick = {
                    navController.navigate(Screen.PersonalDataForm.route)
                },
                onVitalSignsClick = {
                    navController.navigate(Screen.VitalSignsForm.route)
                },
                onHealthQuestionClick = {
                    navController.navigate(Screen.HealthQuestionForm.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PersonalDataForm.route) {
            // Get existing data from state and UserDataStore to pre-fill the form
            val currentEmail = AuthState.currentUserEmail
            val existingContactData = remember(currentEmail) {
                currentEmail?.let { UserDataStore.getDonorContactData(it) }
            }
            val existingPersonalData = DonorProfileState.savedPersonalData
            PersonalDataFormScreen(
                firstName = existingContactData?.firstName ?: "",
                lastName = existingContactData?.lastName ?: "",
                email = existingContactData?.email ?: currentEmail ?: "",
                phone = existingContactData?.phone ?: "",
                bloodType = existingContactData?.bloodType,
                birthdate = existingPersonalData?.birthdate,
                gender = existingPersonalData?.gender,
                weight = existingPersonalData?.weight ?: 0f,
                emergencyContact = existingPersonalData?.emergencyContact ?: "",
                onSaveClick = { firstName, lastName, email, phone, bloodType, birthdate, gender, weight, emergencyContact ->
                    // Data is already saved to DonorProfileState and UserDataStore in the screen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.VitalSignsForm.route) {
            // Get existing data from state to pre-fill the form
            val existingVitalSigns = DonorProfileState.savedVitalSigns
            VitalSignsFormScreen(
                hemoglobinLevel = existingVitalSigns?.hemoglobinLevel ?: 0f,
                bodyTemperature = existingVitalSigns?.bodyTemperature ?: 0f,
                pulseRate = existingVitalSigns?.pulseRate ?: 0f,
                onSaveClick = { hemoglobinLevel, bodyTemperature, pulseRate ->
                    // Data is already saved to DonorProfileState in the screen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.HealthQuestionForm.route) {
            // Get existing health questions from state
            val existingHealthQuestions = DonorProfileState.savedHealthQuestions
            HealthQuestionFormScreen(
                hasTattoosWithinLast6Months = existingHealthQuestions?.get("hasTattoosWithinLast6Months")
                    ?: false,
                hasSurgeryWithinLast6_12Months = existingHealthQuestions?.get("hasSurgeryWithinLast6_12Months")
                    ?: false,
                hasChronicalIllness = existingHealthQuestions?.get("hasChronicalIllness") ?: false,
                hasTravelledWithinLast3Months = existingHealthQuestions?.get("hasTravelledWithinLast3Months")
                    ?: false,
                hasPiercingWithinLast7Months = existingHealthQuestions?.get("hasPiercingWithinLast7Months")
                    ?: false,
                isOnMedication = existingHealthQuestions?.get("isOnMedication") ?: false,
                onSaveClick = { hasTattoos, hasSurgery, hasChronicalIllness, hasTravelled, hasPiercing, isOnMedication ->
                    // Data is already saved to DonorProfileState in the screen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddBloodBank.route) {
            AddBloodBankScreen(
                onBloodBankSelected = { bloodBankId ->
                    // TODO: Save blood bank affiliation to ViewModel/Backend
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onSaveClick = { currentPassword, newPassword, confirmPassword ->
                    // TODO: Save password change to ViewModel/Backend
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.DoctorDashboard.route) {
            DoctorDashboardScreen(
                onProfileClick = {
                    navController.navigate(Screen.DoctorProfile.route)
                },
                onCreateRequestClick = {
                    navController.navigate(Screen.CreateBloodRequest.route)
                },
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestDetails.createRoute(requestId))
                },
                onBackClick = {
                    // If there's a back stack, pop it, otherwise navigate to home
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.DoctorDashboard.route) {
                        if (currentRoute != Screen.DoctorDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.DoctorDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DoctorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                bloodRequests = emptyList() // TODO: Load from ViewModel
            )
        }

        composable(Screen.CreateBloodRequest.route) {
            CreateBloodRequestScreen(
                onSaveClick = { bloodType, quantity, notes, selectedBloodBankEmails ->
                    // Data is already saved in CreateBloodRequestScreen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.BloodRequests.route) {
            BloodRequestsScreen(
                onRequestClick = { requestId ->
                    navController.navigate(Screen.RequestDetails.createRoute(requestId))
                },
                onCreateRequestClick = {
                    navController.navigate(Screen.CreateBloodRequest.route)
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodRequests.route) {
                        if (currentRoute != Screen.BloodRequests.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.DoctorDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DoctorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.DoctorProfile.route) {
            DoctorProfileScreen(
                doctor = null, // TODO: Load from ViewModel
                onEditProfileClick = {
                    navController.navigate(Screen.EditDoctorProfile.route)
                },
                onChangePasswordClick = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onLogoutClick = {
                    // Clear user session
                    AuthState.logout()
                    // Navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.DoctorDashboard.route) {
                        if (currentRoute != Screen.DoctorDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.DoctorDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.DoctorDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.EditDoctorProfile.route) {
            EditDoctorProfileScreen(
                onSaveClick = { firstName, lastName, email, phone, hospitalName, specialization, medicalLicenseNumber ->
                    // Data is already saved to DoctorProfileState in the screen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.BloodBankDashboard.route) {
            BloodBankDashboardScreen(
                onProfileClick = {
                    navController.navigate(Screen.BloodBankProfile.route)
                },
                onManageRequestsClick = {
                    navController.navigate(Screen.ManageRequests.route)
                },
                onStockManagementClick = {
                    navController.navigate(Screen.StockManagement.route)
                },
                onAlertResponsesClick = {
                    navController.navigate(Screen.AlertResponses.route)
                },
                onAffiliatedDonorsClick = {
                    navController.navigate(Screen.AffiliatedDonors.route)
                },
                onCreateCustomAlertClick = {
                    navController.navigate(Screen.CreateCustomAlert.route)
                },
                onCreateNotificationClick = {
                    navController.navigate(Screen.CreateNotification.route)
                },
                currentRoute = navController.currentDestination?.route
                    ?: Screen.BloodBankDashboard.route,
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodBankDashboard.route) {
                        // If already on dashboard, pop back stack to root dashboard
                        if (currentRoute == Screen.BloodBankDashboard.route) {
                            // Already on dashboard, pop any nested screens
                            navController.popBackStack(
                                Screen.BloodBankDashboard.route,
                                inclusive = false
                            )
                        } else {
                            // Navigate to dashboard and pop back stack
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                bloodRequests = emptyList(), // TODO: Load from ViewModel
                stock = emptyList<StockByType>() // TODO: Load from ViewModel
            )
        }

        composable(Screen.ManageRequests.route) {
            ManageRequestsScreen(
                bloodRequests = emptyList(), // TODO: Load from ViewModel
                onRequestClick = { requestId ->
                    // TODO: Navigate to request details
                },
                onSendAlertClick = { request ->
                    // Convert UUID to Int for navigation (using hashCode as a simple conversion)
                    val requestIdInt = request.id?.hashCode() ?: 0
                    navController.navigate(Screen.SendAlert.createRoute(requestIdInt))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodBankDashboard.route) {
                        if (currentRoute != Screen.BloodBankDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.StockManagement.route) {
            StockManagementScreen(
                stock = emptyList(), // TODO: Load from ViewModel
                onStockItemClick = { stockId ->
                    // TODO: Navigate to stock details
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodBankDashboard.route) {
                        if (currentRoute != Screen.BloodBankDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.BloodBankProfile.route) {
            BloodBankProfileScreen(
                bloodBank = null, // TODO: Load from ViewModel
                onEditProfileClick = {
                    navController.navigate(Screen.EditBloodBankProfile.route)
                },
                onChangePasswordClick = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onLogoutClick = {
                    // Clear user session
                    AuthState.logout()
                    // Navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodBankDashboard.route) {
                        if (currentRoute != Screen.BloodBankDashboard.route) {
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.EditBloodBankProfile.route) {
            EditBloodBankProfileScreen(
                onSaveClick = { bloodBankName, email, phone, address, licenseNumber ->
                    // Data is already saved to BloodBankProfileState in the screen
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.SendAlert.route,
            arguments = listOf(navArgument("requestId") { type = NavType.IntType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getInt("requestId") ?: 0
            // Find request by matching requestIdInt
            val requestData = SharedRequestsState.getAllRequestsData().firstOrNull {
                it.getRequestIdInt() == requestId
            }

            // Get blood bank name
            val bloodBankData by BloodBankProfileState::savedBloodBankData
            val bloodBankName = bloodBankData?.bloodBankName ?: "Blood Bank"

            if (requestData != null) {
                SendAlertScreen(
                    requestData = requestData,
                    bloodBankName = bloodBankName,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAlertSent = {
                        navController.popBackStack()
                    }
                )
            } else {
                // Request not found, go back
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable(Screen.AlertResponses.route) {
            val currentRoute =
                navController.currentDestination?.route ?: Screen.AlertResponses.route
            AlertResponsesScreen(
                requestId = null, // Show all responses
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    val currentRoute = navController.currentDestination?.route
                    if (route == Screen.BloodBankDashboard.route) {
                        if (currentRoute == Screen.BloodBankDashboard.route) {
                            navController.popBackStack(
                                Screen.BloodBankDashboard.route,
                                inclusive = false
                            )
                        } else {
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                showBottomNavigation = true // Always show bottom navigation for this screen
            )
        }

        composable(Screen.BloodBankSettings.route) {
            val currentRoute =
                navController.currentDestination?.route ?: Screen.BloodBankSettings.route
            BloodBankSettingsScreen(
                bloodBank = null, // TODO: Load from ViewModel
                onEditProfileClick = {
                    navController.navigate(Screen.EditBloodBankProfile.route)
                },
                onChangePasswordClick = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onLogoutClick = {
                    // Clear user session
                    AuthState.logout()
                    // Navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route == Screen.BloodBankDashboard.route) {
                        if (currentRoute == Screen.BloodBankDashboard.route) {
                            navController.popBackStack(
                                Screen.BloodBankDashboard.route,
                                inclusive = false
                            )
                        } else {
                            navController.navigate(route) {
                                popUpTo(Screen.BloodBankDashboard.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(Screen.BloodBankDashboard.route) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        composable(Screen.AffiliatedDonors.route) {
            AffiliatedDonorsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CreateCustomAlert.route) {
            CreateCustomAlertScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAlertSent = {
                    navController.popBackStack()
                }
            )
        }

        // TODO: Add more routes for other screens
    }
}
