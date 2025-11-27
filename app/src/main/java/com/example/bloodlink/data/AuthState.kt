package com.example.bloodlink.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.bloodlink.data.model.dtos.requests.LoginRequest
import com.example.bloodlink.data.model.dtos.requests.RegisterRequest
import com.example.bloodlink.data.model.dtos.responses.AuthenticationResponse
import com.example.bloodlink.data.model.enums.UserRole
import com.example.bloodlink.data.model.metiers.User
import com.example.bloodlink.retrofit.RetrofitInstance
import com.example.bloodlink.retrofit.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

// Lightweight wrapper to return either data or a user-facing error message
data class AuthResult<T>(val data: T? = null, val error: String? = null)

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

    private fun mapExceptionToMessage(e: Exception): String {
        return when (e) {
            is HttpException -> {
                // Try to parse error message from response body
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.d("AuthState", "Error body: $errorBody")
                    
                    // Try to extract message from JSON error response
                    if (errorBody != null && errorBody.contains("message")) {
                        val messageStart = errorBody.indexOf("\"message\":\"") + 11
                        val messageEnd = errorBody.indexOf("\"", messageStart)
                        if (messageStart > 10 && messageEnd > messageStart) {
                            val message = errorBody.substring(messageStart, messageEnd)
                            return message
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("AuthState", "Failed to parse error body: ${ex.message}")
                }
                
                // Fallback to generic messages
                val code = e.code()
                when {
                    code in 500..599 -> "Serveur indisponible ($code), réessaie plus tard."
                    code == 401 || code == 403 -> "Accès refusé"
                    code == 400 -> "Email ou mot de passe incorrect."
                    else -> "Requête refusée par le serveur ($code)."
                }
            }
            is IOException -> "Connexion impossible. Vérifie ta connexion internet."
            else -> e.message ?: "Erreur inconnue. Réessaie."
        }
    }

    suspend fun getCurrentUserDetails(context: Context): User? {
        val tokenManager = TokenManager(context)
        return try {
            Log.d("AuthState", "=== FETCHING USER DETAILS ===")
            val token = tokenManager.tokenFlow.first()
            Log.d("AuthState", "Token available: ${token != null}")
            if (token != null) {
                Log.d("AuthState", "Token: ${token.take(20)}...")
            }
            
            Log.d("AuthState", "Calling /api/v1/users/me...")
            val user = RetrofitInstance.getUserApi(context).getMe()
            
            Log.d("AuthState", "=== USER DETAILS RECEIVED ===")
            Log.d("AuthState", "User type: ${user?.javaClass?.simpleName}")
            Log.d("AuthState", "User email: ${user?.username}")
            
            user
        } catch (e : Exception) {
            Log.e("AuthState", "=== ERROR FETCHING USER DETAILS ===")
            Log.e("AuthState", "Error type: ${e.javaClass.simpleName}")
            Log.e("AuthState", "Error message: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Register a new user
    suspend fun registerUser(request: RegisterRequest, context: Context): AuthResult<AuthenticationResponse> {
        val tokenManager = TokenManager(context)
        try {
            Log.d("AuthState", "=== REGISTRATION START ===")
            Log.d("AuthState", "Email: ${request.email}")
            Log.d("AuthState", "Phone: ${request.phoneNumber}")
            Log.d("AuthState", "Role: ${request.userRole}")
            Log.d("AuthState", "Sending request to backend...")
            
            val user = RetrofitInstance.getAuthenticationApi(context).signUp(request)
            
            Log.d("AuthState", "=== RESPONSE RECEIVED ===")
            if (user != null) {
                Log.d("AuthState", "User token: ${user.token.take(20)}...")
                Log.d("AuthState", "User email: ${user.email}")
                Log.d("AuthState", "User role: ${user.role}")
                Log.d("AuthState", "Saving token...")
                
                tokenManager.saveToken(user.token)
                currentUserEmail = user.email
                currentUserRole = user.role
                
                Log.d("AuthState", "=== REGISTRATION SUCCESS ===")
                return AuthResult(data = user)
            } else {
                Log.e("AuthState", "User is null after signup")
                return AuthResult(error = "Inscription échouée. Merci de vérifier les informations saisies.")
            }
        } catch (e: Exception) {
            Log.e("AuthState", "=== REGISTRATION ERROR ===")
            Log.e("AuthState", "Error type: ${e.javaClass.simpleName}")
            Log.e("AuthState", "Error message: ${e.message}")
            Log.e("AuthState", "Stack trace:")
            e.printStackTrace()
            return AuthResult(error = mapExceptionToMessage(e))
        }
    }

    // Login: Check credentials and return user role if valid
    suspend fun login(email: String, password: String, context: Context): AuthResult<AuthenticationResponse> {
        //val user = registeredUsers[email.lowercase()]
        /*return if (user != null && user.password == password) {
            currentUserEmail = email.lowercase()
            currentUserRole = user.role
            user.role
        } else {
            null
        }
        return user*/
        val tokenManager = TokenManager(context)
        return  try {
            Log.d("AuthState", "Start Sending Login Request")
            val user = RetrofitInstance.getAuthenticationApi(context).authenticate(
                LoginRequest(email, password)
            )
            if (user != null) {
                tokenManager.saveToken(user.token)
                currentUserEmail = user.email
                currentUserRole = user.role
                Log.d("AuthState", "Login successful: $email")
                 AuthResult(data = user)
            } else {
                Log.d("AuthState", "Login returned null")
                AuthResult(error = "Email ou mot de passe incorrect.")
            }
               // return null // Handle unsuccessful login (e.g., wrong credentials)
        } catch (e: Exception) {
            // For a production app, you should log the error e
            e.printStackTrace()
            Log.e("AuthState", "Login failed: ${e.message}")
            AuthResult(error = mapExceptionToMessage(e))
        }
    }

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


    // Logout: Clear current session
    fun logout() {
        currentUserEmail = null
        currentUserRole = null
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean = currentUserEmail != null

    // Check if email is already registered
//    fun isEmailRegistered(email: String): Boolean {
//        return registeredUsers.containsKey(email.lowercase())
//    }
}


//// Register a new user
//suspend fun registerUser(request: RegisterRequest, context: Context): AuthenticationResponse? {
//    /*registeredUsers[email.lowercase()] = RegisteredUser(
//        email = email.lowercase(),
//        password = password,
//        role = role
//    )*/
//    val tokenManager = TokenManager(context)
//    try {
//        Log.e("AuthState", "Start Sending Registration Request")
//        val user = RetrofitInstance.getAuthenticationApi(context).signUp(request)
//        if (user != null) {
//            tokenManager.saveToken(user.token)
//            currentUserEmail = user.email
//            currentUserRole = user.role
//            return user
//        } else {
//            return null
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        Log.e("AuthState", "Registration failed: ${e.message}")
//        return null
//    }
//
//}



    // Register a new user
//    suspend fun registerUser(email: String, password: String, role: UserRole, name: String, surname: String, phoneNumber: String, lastDonationDate: LocalDate, bloodType: BloodType, orderLicense: UUID, speciality: String, hospitalName: String, bloodBnakName: String, address: String, licenseNumber: UUID, context: Context): AuthenticationResponse? {
//        registeredUsers[email.lowercase()] = RegisteredUser(
//            email = email.lowercase(),
//            password = password,
//            role = role
//        )
//
//        try {
//            Log.e("AuthState", "Start Sending Registration Request")
//            val user = RetrofitInstance.getAuthenticationApi(context).signUp(
//                RegisterRequest(
//                    email = email,
//                    password = password,
//                    userRole = role,
//                    name = name,
//                    surname = surname,
//                    phoneNumber = phoneNumber,
//                    LastDonationDate = lastDonationDate.toString(),
//                    bloodType = bloodType,
//                    orderLicense = orderLicense,
//                    speciality = speciality,
//                    hospitalName = hospitalName,
//                    bloodBankName = bloodBnakName,
//                    address = address,
//                    licenseNumber = licenseNumber
//                )
//            )
//            if (user != null) {
//                currentUserEmail = user.email
//                currentUserRole = user.role
//                return user
//            } else {
//                return null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("AuthState", "Registration failed: ${e.message}")
//            return null
//        }
//
//    }



//    // Register a new user
//    suspend fun registerUser(request: RegisterRequest, context: Context): AuthenticationResponse? {
//        registeredUsers[email.lowercase()] = RegisteredUser(
//            email = email.lowercase(),
//            password = password,
//            role = role
//        )
//        val tokenManager = TokenManager(context)
//        try {
//            Log.e("AuthState", "Start Sending Registration Request")
//            val user = RetrofitInstance.getAuthenticationApi(context).signUp(request)
//            if (user != null) {
//                tokenManager.saveToken(user.token)
//                currentUserEmail = user.email
//                currentUserRole = user.role
//                return user
//            } else {
//                return null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("AuthState", "Registration failed: ${e.message}")
//            return null
//        }
//
//    } // A commenter


//    // Login: Check credentials and return user role if valid
//    suspend fun login(email: String, password: String, context: Context): AuthenticationResponse? {
//        //val user = registeredUsers[email.lowercase()]
//        /*return if (user != null && user.password == password) {
//            currentUserEmail = email.lowercase()
//            currentUserRole = user.role
//            user.role
//        } else {
//            null
//        }
//        return user*/
//        try {
//            Log.e("LoginViewModel", "Start Sending Request")
//            val user = RetrofitInstance.getAuthenticationApi(context).authenticate(LoginRequest(email, password))
//            if (user != null)
//                return user // Login successful
//            else
//                return null // Handle unsuccessful login (e.g., wrong credentials)
//        } catch (e: Exception) {
//            // For a production app, you should log the error e
//            e.printStackTrace()
//            Log.e("LoginViewModel", "Login failed: ${e.message}")
//            return null
//        }
//    }
//
//   suspend fun getCurrentUser(context: Context): User? {
//        return withContext(Dispatchers.IO){
//            try {
//                RetrofitInstance.getUserApi(context).getMe()
//            }catch (e: java.lang.Exception) {
//                Log.e("AuthState", "Registration failed: ${e.message}", e)
//                null
//            }
//        }
//    }
//
//
//    // Logout: Clear current session
//    fun logout() {
//        currentUserEmail = null
//        currentUserRole = null
//    }
//
//    // Check if user is logged in
//    fun isLoggedIn(): Boolean = currentUserEmail != null
//
//    // Check if email is already registered
////    fun isEmailRegistered(email: String): Boolean {
////        return registeredUsers.containsKey(email.lowercase())
////    }
///*====================================================================================================================================================================================================================*/



