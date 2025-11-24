package com.example.bloodlink.data.model.enums

enum class UserRole(val value: String) {
    DONOR("donor"),
    DOCTOR("doctor"),
    BLOOD_BANK("bloodBank");
    companion object {
        fun fromString(value: String): UserRole? {
            return entries.find { it.value.equals(value, ignoreCase = true) }
        }
    }
}

