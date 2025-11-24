package com.example.bloodlink.navigation

sealed class Screen(val route: String) {
    // Home
    object Home : Screen("home")
    
    // Authentication
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object CheckEmail : Screen("check_email/{email}") {
        fun createRoute(email: String) = "check_email/$email"
    }
    object SignUp : Screen("signup")
    object SignUpDonor : Screen("signup_donor")
    object SignUpDoctor : Screen("signup_doctor")
    object SignUpBloodBank : Screen("signup_blood_bank")
    object RoleSelection : Screen("role_selection")

    // Donor Screens
    object DonorDashboard : Screen("donor_dashboard")
    object DonorProfile : Screen("donor_profile")
    object HealthProfile : Screen("health_profile")
    object PersonalDataForm : Screen("personal_data_form")
    object VitalSignsForm : Screen("vital_signs_form")
    object HealthQuestionForm : Screen("health_question_form")
    object DonorNotifications : Screen("donor_notifications")
    object DonationHistory : Screen("donation_history")
    object DonationDetails : Screen("donation_details/{donationId}") {
        fun createRoute(donationId: Int) = "donation_details/$donationId"
    }
    object AddBloodBank : Screen("add_blood_bank")
    object ChangePassword : Screen("change_password")

    // Doctor Screens
    object DoctorDashboard : Screen("doctor_dashboard")
    object DoctorProfile : Screen("doctor_profile")
    object EditDoctorProfile : Screen("edit_doctor_profile")
    object BloodRequests : Screen("blood_requests")
    object CreateBloodRequest : Screen("create_blood_request")
    object RequestDetails : Screen("request_details/{requestId}") {
        fun createRoute(requestId: Int) = "request_details/$requestId"
    }

    // Blood Bank Screens
    object BloodBankDashboard : Screen("blood_bank_dashboard")
    object BloodBankProfile : Screen("blood_bank_profile")
    object BloodBankSettings : Screen("blood_bank_settings")
    object EditBloodBankProfile : Screen("edit_blood_bank_profile")
    object ManageRequests : Screen("manage_requests")
    object StockManagement : Screen("stock_management")
    object CreateNotification : Screen("create_notification")
    object SendAlert : Screen("send_alert/{requestId}") {
        fun createRoute(requestId: Int) = "send_alert/$requestId"
    }
    object AlertResponses : Screen("alert_responses")
    object AffiliatedDonors : Screen("affiliated_donors")
    object CreateCustomAlert : Screen("create_custom_alert")
}

