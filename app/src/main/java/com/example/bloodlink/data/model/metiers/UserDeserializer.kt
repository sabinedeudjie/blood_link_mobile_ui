package com.example.bloodlink.data.model.metiers

import android.util.Log
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.enums.UserRole
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class UserDeserializer : JsonDeserializer<User> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): User {
        Log.d("UserDeserializer", "=== DESERIALIZING USER ===")
        
        if (json == null || !json.isJsonObject) {
            Log.e("UserDeserializer", "JSON is null or not an object")
            throw JsonParseException("Invalid JSON for User")
        }
        
        val jsonObject = json.asJsonObject
        Log.d("UserDeserializer", "JSON: $jsonObject")
        
        // Extract userRole to determine which subclass to create
        val roleString = jsonObject.get("userRole")?.asString
        Log.d("UserDeserializer", "Role string: $roleString")
        
        val userRole = roleString?.let { UserRole.fromString(it) }
        Log.d("UserDeserializer", "Parsed role: $userRole")
        
        return when (userRole) {
            UserRole.DONOR -> {
                Log.d("UserDeserializer", "Creating Donor instance")
                createDonor(jsonObject, context)
            }
            UserRole.DOCTOR -> {
                Log.d("UserDeserializer", "Creating Doctor instance")
                createDoctor(jsonObject, context)
            }
            UserRole.BLOOD_BANK -> {
                Log.d("UserDeserializer", "Creating BloodBank instance")
                createBloodBank(jsonObject, context)
            }
            else -> {
                Log.w("UserDeserializer", "Unknown or null role, creating base User")
                createBaseUser(jsonObject, context)
            }
        }
    }
    
    private fun createDonor(jsonObject: JsonObject, context: JsonDeserializationContext?): Donor {
        val donor = Donor()
        
        // Set User fields
        setUserFields(donor, jsonObject, context)
        
        // Set Person fields
        setPersonFields(donor, jsonObject)
        
        // Set Donor-specific fields
        jsonObject.get("bloodType")?.asString?.let { bloodTypeStr ->
            try {
                val bloodType = BloodType.fromString(bloodTypeStr)
                val field = findFieldInHierarchy(donor.javaClass, "bloodType")
                field?.apply {
                    isAccessible = true
                    set(donor, bloodType)
                }
                Log.d("UserDeserializer", "Set bloodType: $bloodType")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting bloodType: ${e.message}")
            }
        }
        
        jsonObject.get("lastDonationDate")?.asString?.let { dateStr ->
            try {
                val date = LocalDate.parse(dateStr)
                val field = findFieldInHierarchy(donor.javaClass, "lastDonationDate")
                field?.apply {
                    isAccessible = true
                    set(donor, date)
                }
                Log.d("UserDeserializer", "Set lastDonationDate: $date")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting lastDonationDate: ${e.message}")
            }
        }
        
        Log.d("UserDeserializer", "Donor created successfully")
        return donor
    }
    
    private fun createDoctor(jsonObject: JsonObject, context: JsonDeserializationContext?): Doctor {
        val doctor = Doctor()
        
        // Set User fields
        setUserFields(doctor, jsonObject, context)
        
        // Set Person fields
        setPersonFields(doctor, jsonObject)
        
        // Set Doctor-specific fields
        jsonObject.get("hospitalName")?.asString?.let { hospital ->
            try {
                val field = findFieldInHierarchy(doctor.javaClass, "hospitalName")
                field?.apply {
                    isAccessible = true
                    set(doctor, hospital)
                }
                Log.d("UserDeserializer", "Set hospitalName: $hospital")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting hospitalName: ${e.message}")
            }
        }
        
        jsonObject.get("speciality")?.asString?.let { spec ->
            try {
                val field = findFieldInHierarchy(doctor.javaClass, "speciality")
                field?.apply {
                    isAccessible = true
                    set(doctor, spec)
                }
                Log.d("UserDeserializer", "Set speciality: $spec")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting speciality: ${e.message}")
            }
        }
        
        jsonObject.get("licenseNumber")?.asString?.let { license ->
            try {
                val uuid = UUID.fromString(license)
                val field = findFieldInHierarchy(doctor.javaClass, "licenseNumber")
                field?.apply {
                    isAccessible = true
                    set(doctor, uuid)
                }
                Log.d("UserDeserializer", "Set licenseNumber: $uuid")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting licenseNumber: ${e.message}")
            }
        }
        
        Log.d("UserDeserializer", "Doctor created successfully")
        return doctor
    }
    
    private fun createBloodBank(jsonObject: JsonObject, context: JsonDeserializationContext?): BloodBank {
        val bloodBank = BloodBank()
        
        // Set User fields
        setUserFields(bloodBank, jsonObject, context)
        
        // Set BloodBank-specific fields
        jsonObject.get("bloodBankName")?.asString?.let { name ->
            try {
                val field = findFieldInHierarchy(bloodBank.javaClass, "bloodBankName")
                field?.apply {
                    isAccessible = true
                    set(bloodBank, name)
                }
                Log.d("UserDeserializer", "Set bloodBankName: $name")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting bloodBankName: ${e.message}")
            }
        }
        
        jsonObject.get("address")?.asString?.let { addr ->
            try {
                val field = findFieldInHierarchy(bloodBank.javaClass, "address")
                field?.apply {
                    isAccessible = true
                    set(bloodBank, addr)
                }
                Log.d("UserDeserializer", "Set address: $addr")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting address: ${e.message}")
            }
        }
        
        jsonObject.get("licenseNumber")?.asString?.let { license ->
            try {
                val uuid = UUID.fromString(license)
                val field = findFieldInHierarchy(bloodBank.javaClass, "licenseNumber")
                field?.apply {
                    isAccessible = true
                    set(bloodBank, uuid)
                }
                Log.d("UserDeserializer", "Set licenseNumber: $uuid")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting licenseNumber: ${e.message}")
            }
        }
        
        Log.d("UserDeserializer", "BloodBank created successfully")
        return bloodBank
    }
    
    private fun createBaseUser(jsonObject: JsonObject, context: JsonDeserializationContext?): User {
        val user = User()
        setUserFields(user, jsonObject, context)
        Log.d("UserDeserializer", "Base User created")
        return user
    }
    
    private fun setUserFields(user: User, jsonObject: JsonObject, context: JsonDeserializationContext?) {
        // Set username (email) - chercher dans la hiérarchie de classes
        jsonObject.get("email")?.asString?.let { email ->
            try {
                val field = findFieldInHierarchy(user.javaClass, "username")
                field?.apply {
                    isAccessible = true
                    set(user, email)
                }
                Log.d("UserDeserializer", "Set username: $email")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting username: ${e.message}")
            }
        }
        
        // Set phoneNumber
        jsonObject.get("phoneNumber")?.asString?.let { phone ->
            try {
                val field = findFieldInHierarchy(user.javaClass, "phoneNumber")
                field?.apply {
                    isAccessible = true
                    set(user, phone)
                }
                Log.d("UserDeserializer", "Set phoneNumber: $phone")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting phoneNumber: ${e.message}")
            }
        }
        
        // Set userRole
        jsonObject.get("userRole")?.asString?.let { roleStr ->
            val role = UserRole.fromString(roleStr)
            user.userRole = role
            Log.d("UserDeserializer", "Set userRole: $role")
        }
        
        // Set id if present
        jsonObject.get("id")?.asString?.let { idStr ->
            try {
                val uuid = UUID.fromString(idStr)
                val field = findFieldInHierarchy(user.javaClass, "id")
                field?.apply {
                    isAccessible = true
                    set(user, uuid)
                }
                Log.d("UserDeserializer", "Set id: $uuid")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting id: ${e.message}")
            }
        }
    }
    
    private fun findFieldInHierarchy(clazz: Class<*>, fieldName: String): java.lang.reflect.Field? {
        var currentClass: Class<*>? = clazz
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName)
            } catch (e: NoSuchFieldException) {
                currentClass = currentClass.superclass
            }
        }
        return null
    }
    
    private fun setPersonFields(person: Person, jsonObject: JsonObject) {
        // Set name
        jsonObject.get("name")?.asString?.let { name ->
            try {
                val field = findFieldInHierarchy(person.javaClass, "name")
                field?.apply {
                    isAccessible = true
                    set(person, name)
                }
                Log.d("UserDeserializer", "Set name: $name")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting name: ${e.message}")
            }
        }
        
        // Set surname
        jsonObject.get("surname")?.asString?.let { surname ->
            try {
                val field = findFieldInHierarchy(person.javaClass, "surname")
                field?.apply {
                    isAccessible = true
                    set(person, surname)
                }
                Log.d("UserDeserializer", "Set surname: $surname")
            } catch (e: Exception) {
                Log.e("UserDeserializer", "Error setting surname: ${e.message}")
            }
        }
    }
}
