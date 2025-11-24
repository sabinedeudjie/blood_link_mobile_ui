package com.example.bloodlink.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.bloodlink.data.model.dtos.requests.LoginRequest
import com.example.bloodlink.data.model.dtos.requests.RegisterRequest
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.data.model.metiers.User
import com.example.bloodlink.retrofit.RetrofitInstance
import com.example.bloodlink.retrofit.TokenManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

// Data class to store registered user information
data class RegisteredUser(
    val email: String,
    val password: String,
    val role: UserRole
)

// Global authentication state to manage registered users and current session
object AuthState {
    // Store all registered users (email -> RegisteredUser)
    private val registeredUsers = mutableMapOf<String, RegisteredUser>()
    
    // Current logged-in user
    var currentUserEmail: String? by mutableStateOf(null)
    var currentUserRole: UserRole? by mutableStateOf(null)
    
    // Register a new user
    suspend fun registerUser(email: String, password: String, role: UserRole, name: String, surname: String, phoneNumber: String, lastDonationDate: LocalDate, bloodType: BloodType, orderLicense: UUID, speciality: String, hospitalName: String, bloodBnakName: String, address: String, licenseNumber: UUID, context: Context): AuthenticationResponse? {
        /*registeredUsers[email.lowercase()] = RegisteredUser(
            email = email.lowercase(),
            password = password,
            role = role
        )*/

        try {
            Log.e("AuthState", "Start Sending Registration Request")
            val user = RetrofitInstance.getAuthenticationApi(context).signUp(
                RegisterRequest(
                    email = email,
                    password = password,
                    userRole = role,
                    name = name,
                    surname = surname,
                    phoneNumber = phoneNumber,
                    LastDonationDate = lastDonationDate.toString(),
                    bloodType = bloodType,
                    orderLicense = orderLicense,
                    speciality = speciality,
                    hospitalName = hospitalName,
                    bloodBankName = bloodBnakName,
                    address = address,
                    licenseNumber = licenseNumber
                )
            )
            if (user != null) {
                currentUserEmail = user.email
                currentUserRole = user.role
                return user
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AuthState", "Registration failed: ${e.message}")
            return null
        }

    }

    // Register a new user
    suspend fun registerUser(request: RegisterRequest, context: Context): AuthenticationResponse? {
        /*registeredUsers[email.lowercase()] = RegisteredUser(
            email = email.lowercase(),
            password = password,
            role = role
        )*/
        val tokenManager = TokenManager(context)
        try {
            Log.e("AuthState", "Start Sending Registration Request")
            val user = RetrofitInstance.getAuthenticationApi(context).signUp(request)
            if (user != null) {
                tokenManager.saveToken(user.token)
                currentUserEmail = user.email
                currentUserRole = user.role
                return user
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AuthState", "Registration failed: ${e.message}")
            return null
        }

    }

    // Login: Check credentials and return user role if valid
    suspend fun login(email: String, password: String, context: Context): AuthenticationResponse? {
        //val user = registeredUsers[email.lowercase()]
        /*return if (user != null && user.password == password) {
            currentUserEmail = email.lowercase()
            currentUserRole = user.role
            user.role
        } else {
            null
        }
        return user*/
        try {
            Log.e("LoginViewModel", "Start Sending Request")
            val user = RetrofitInstance.getAuthenticationApi(context).authenticate(LoginRequest(email, password))
            if (user != null)
                return user // Login successful
            else
                return null // Handle unsuccessful login (e.g., wrong credentials)
        } catch (e: Exception) {
            // For a production app, you should log the error e
            e.printStackTrace()
            Log.e("LoginViewModel", "Login failed: ${e.message}")
            return null
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getCurrentUser(context: Context): User? {
        return withContext(Dispatchers.IO){
            try {
                RetrofitInstance.getUserApi(context).getMe()
            }catch (e: java.lang.Exception) {
                Log.e("AuthState", "Registration failed: ${e.message}", e)
                null
            }
        }
    }
    /*fun getCurrentUser(context: Context): User? {
        var user: User? = null
        GlobalScope.launch {
            try {
                user = RetrofitInstance.getUserApi(context).getMe()
            }
            catch (e: Exception) {
                e.printStackTrace()
            } }
        return user
    }*/

    // Logout: Clear current session
    fun logout() {
        currentUserEmail = null
        currentUserRole = null
    }
    
    // Check if user is logged in
    fun isLoggedIn(): Boolean = currentUserEmail != null
    
    // Check if email is already registered
    fun isEmailRegistered(email: String): Boolean {
        return registeredUsers.containsKey(email.lowercase())
    }

}

