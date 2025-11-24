package com.example.bloodlink.data

import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.ui.screens.donor.PersonalDataForm
import com.example.bloodlink.ui.screens.donor.DonorDataForm
import com.example.bloodlink.ui.screens.donor.VitalSignsForm
import com.example.bloodlink.ui.screens.doctor.DoctorDataForm
import com.example.bloodlink.ui.screens.bloodbank.BloodBankDataForm

// Store user profile data by email for login retrieval
object UserDataStore {
    // Store donor contact data by email (firstName, lastName, email, phone, bloodType)
    private val donorContactDataMap = mutableMapOf<String, DonorDataForm>()
    
    // Store donor personal data by email (birthdate, gender, weight, emergencyContact)
    private val donorDataMap = mutableMapOf<String, PersonalDataForm>()
    
    // Store doctor data by email
    private val doctorDataMap = mutableMapOf<String, DoctorDataForm>()
    
    // Store blood bank data by email
    private val bloodBankDataMap = mutableMapOf<String, BloodBankDataForm>()
    
    // Store passwords by email (for each role)
    private val donorPasswords = mutableMapOf<String, String>()
    private val doctorPasswords = mutableMapOf<String, String>()
    private val bloodBankPasswords = mutableMapOf<String, String>()
    
    // Store donor eligibility status by email
    private val donorEligibility = mutableMapOf<String, Boolean>()
    
    // Store donor vital signs by email
    private val donorVitalSigns = mutableMapOf<String, VitalSignsForm>()
    
    // Store donor health questions by email
    private val donorHealthQuestions = mutableMapOf<String, Map<String, Boolean>>()
    
    // Store donor blood bank affiliations by email (store emails instead of BloodBank objects)
    private val donorBloodBankAffiliations = mutableMapOf<String, MutableList<String>>()
    
    // Save donor contact data (firstName, lastName, email, phone, bloodType)
    fun saveDonorContactData(email: String, contactData: DonorDataForm, password: String) {
        val emailLower = email.lowercase()
        donorContactDataMap[emailLower] = contactData
        donorPasswords[emailLower] = password
    }
    
    // Save donor personal data (birthdate, gender, weight, emergencyContact)
    fun saveDonorPersonalData(email: String, personalData: PersonalDataForm) {
        val emailLower = email.lowercase()
        donorDataMap[emailLower] = personalData
    }
    
    // Save donor data (both contact and personal data)
    fun saveDonorData(email: String, contactData: DonorDataForm, personalData: PersonalDataForm, password: String) {
        val emailLower = email.lowercase()
        donorContactDataMap[emailLower] = contactData
        donorDataMap[emailLower] = personalData
        donorPasswords[emailLower] = password
    }
    
    // Legacy method for backward compatibility
    fun saveDonorData(email: String, data: PersonalDataForm, password: String) {
        val emailLower = email.lowercase()
        donorDataMap[emailLower] = data
        donorPasswords[emailLower] = password
    }
    
    // Save donor vital signs
    fun saveDonorVitalSigns(email: String, vitalSigns: VitalSignsForm) {
        donorVitalSigns[email.lowercase()] = vitalSigns
    }
    
    // Get donor vital signs
    fun getDonorVitalSigns(email: String): VitalSignsForm? {
        return donorVitalSigns[email.lowercase()]
    }
    
    // Save donor health questions
    fun saveDonorHealthQuestions(email: String, healthQuestions: Map<String, Boolean>) {
        donorHealthQuestions[email.lowercase()] = healthQuestions
    }
    
    // Get donor health questions
    fun getDonorHealthQuestions(email: String): Map<String, Boolean>? {
        return donorHealthQuestions[email.lowercase()]
    }
    
    // Save donor blood bank affiliations (store emails)
    fun saveDonorBloodBankAffiliations(email: String, affiliations: List<com.example.bloodlink.data.model.metiers.BloodBank>) {
        // Extract emails from BloodBank objects (using username from User base class)
        // Since BloodBank extends User, we can get the username which should be the email
        val emailList = affiliations.mapNotNull { bloodBank ->
            bloodBank.username // BloodBank extends User, so it has username field
        }
        if (emailList.isNotEmpty()) {
            saveDonorBloodBankAffiliationEmails(email, emailList)
        }
    }
    
    // Save donor blood bank affiliations by email list
    fun saveDonorBloodBankAffiliationEmails(email: String, bloodBankEmails: List<String>) {
        donorBloodBankAffiliations[email.lowercase()] = bloodBankEmails.toMutableList()
    }
    
    // Get donor blood bank affiliations (return emails)
    fun getDonorBloodBankAffiliationEmails(email: String): List<String> {
        return donorBloodBankAffiliations[email.lowercase()]?.toList() ?: emptyList()
    }
    
    // Get donor blood bank affiliations (return BloodBank objects by converting emails)
    // Note: Since BloodBank is not a data class, we cannot easily instantiate it
    // This method returns an empty list - use getDonorBloodBankAffiliationEmails() instead
    fun getDonorBloodBankAffiliations(email: String): List<com.example.bloodlink.data.model.metiers.BloodBank> {
        // Cannot instantiate BloodBank objects easily since it's not a data class
        // Return empty list - the UI should use getDonorBloodBankAffiliationEmails() instead
        return emptyList()
    }
    
    // Save doctor data
    fun saveDoctorData(email: String, data: DoctorDataForm, password: String) {
        val emailLower = email.lowercase()
        doctorDataMap[emailLower] = data
        doctorPasswords[emailLower] = password
    }
    
    // Save blood bank data
    fun saveBloodBankData(email: String, data: BloodBankDataForm, password: String) {
        val emailLower = email.lowercase()
        bloodBankDataMap[emailLower] = data
        bloodBankPasswords[emailLower] = password
    }
    
    // Get donor contact data
    fun getDonorContactData(email: String): DonorDataForm? {
        return donorContactDataMap[email.lowercase()]
    }
    
    // Get donor personal data
    fun getDonorPersonalData(email: String): PersonalDataForm? {
        return donorDataMap[email.lowercase()]
    }
    
    // Get donor data (legacy - returns personal data only)
    fun getDonorData(email: String): PersonalDataForm? {
        return donorDataMap[email.lowercase()]
    }
    
    // Get doctor data
    fun getDoctorData(email: String): DoctorDataForm? {
        return doctorDataMap[email.lowercase()]
    }
    
    // Get blood bank data
    fun getBloodBankData(email: String): BloodBankDataForm? {
        return bloodBankDataMap[email.lowercase()]
    }
    
    // Get donor password
    fun getDonorPassword(email: String): String? {
        return donorPasswords[email.lowercase()]
    }
    
    // Get doctor password
    fun getDoctorPassword(email: String): String? {
        return doctorPasswords[email.lowercase()]
    }
    
    // Get blood bank password
    fun getBloodBankPassword(email: String): String? {
        return bloodBankPasswords[email.lowercase()]
    }
    
    // Set donor eligibility status
    fun setDonorEligibility(email: String, isEligible: Boolean) {
        donorEligibility[email.lowercase()] = isEligible
    }
    
    // Get donor eligibility status
    fun getDonorEligibility(email: String): Boolean? {
        return donorEligibility[email.lowercase()]
    }
    
    // Get all eligible donors with a specific blood type
    fun getEligibleDonorsByBloodType(bloodType: com.example.bloodlink.data.model.enums.BloodType): List<String> {
        return donorContactDataMap.filter { (email, contactData) ->
            donorEligibility[email.lowercase()] == true &&
            contactData.bloodType == bloodType
        }.keys.toList()
    }
    
    // Get all donor emails (for finding eligible donors)
    fun getAllDonorEmails(): List<String> {
        return donorContactDataMap.keys.toList()
    }
    
    // Get all registered blood banks (for donors to see and affiliate with)
    fun getAllBloodBanks(): List<BloodBankDataForm> {
        return bloodBankDataMap.values.toList()
    }
    
    // Get blood bank by email
    fun getBloodBankByEmail(email: String): BloodBankDataForm? {
        return bloodBankDataMap[email.lowercase()]
    }
    
    // Get all donors affiliated with a specific blood bank
    fun getAffiliatedDonors(bloodBankEmail: String): List<Pair<String, PersonalDataForm>> {
        val emailLower = bloodBankEmail.lowercase()
        
        // Find all donors who have this blood bank email in their affiliations
        return donorContactDataMap.keys.mapNotNull { donorEmail ->
            val affiliationEmails = donorBloodBankAffiliations[donorEmail.lowercase()]
            val donorData = donorDataMap[donorEmail.lowercase()]
            if (affiliationEmails?.contains(emailLower) == true && donorData != null) {
                Pair(donorEmail, donorData)
            } else {
                null
            }
        }
    }
    
    // Generic method to get password by email and role
    fun getPassword(email: String, role: com.example.bloodlink.data.model.enums.UserRole): String? {
        return when (role) {
            com.example.bloodlink.data.model.enums.UserRole.DONOR -> getDonorPassword(email)
            com.example.bloodlink.data.model.enums.UserRole.DOCTOR -> getDoctorPassword(email)
            com.example.bloodlink.data.model.enums.UserRole.BLOOD_BANK -> getBloodBankPassword(email)
        }
    }
    
    // Generic method to update password by email and role
    fun updatePassword(email: String, newPassword: String, role: com.example.bloodlink.data.model.enums.UserRole) {
        val emailLower = email.lowercase()
        when (role) {
            com.example.bloodlink.data.model.enums.UserRole.DONOR -> {
                donorPasswords[emailLower] = newPassword
                // Also update in the corresponding state if data exists
                val contactData = getDonorContactData(email)
                val personalData = getDonorData(email)
                if (contactData != null && personalData != null) {
                    saveDonorData(email, contactData, personalData, newPassword)
                }
            }
            com.example.bloodlink.data.model.enums.UserRole.DOCTOR -> {
                doctorPasswords[emailLower] = newPassword
                val doctorData = getDoctorData(email)
                if (doctorData != null) {
                    saveDoctorData(email, doctorData, newPassword)
                }
            }
            com.example.bloodlink.data.model.enums.UserRole.BLOOD_BANK -> {
                bloodBankPasswords[emailLower] = newPassword
                val bloodBankData = getBloodBankData(email)
                if (bloodBankData != null) {
                    saveBloodBankData(email, bloodBankData, newPassword)
                }
            }
        }
    }
}

