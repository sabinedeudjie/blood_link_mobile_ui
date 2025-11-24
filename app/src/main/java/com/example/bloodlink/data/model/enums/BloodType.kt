package com.example.bloodlink.data.model.enums

enum class BloodType(val value: String) {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    companion object {
        fun fromString(value: String): BloodType? {
            return entries.find { it.value == value }
        }
    }

    fun getCompatibilityBloodType(other: BloodType): Boolean {
        return compatibilityBloodType(other)
    }

    fun compatibilityBloodType(other: BloodType): Boolean {
        // Universal donor: O-
        if (this == O_NEGATIVE) return true
        
        // Universal recipient: AB+
        if (other == AB_POSITIVE) return true
        
        // Same type
        if (this == other) return true
        
        // O+ can donate to all positive types
        if (this == O_POSITIVE && other.value.contains("+")) return true
        
        // A- can donate to A+ and A-
        if (this == A_NEGATIVE && (other == A_POSITIVE || other == A_NEGATIVE)) return true
        
        // B- can donate to B+ and B-
        if (this == B_NEGATIVE && (other == B_POSITIVE || other == B_NEGATIVE)) return true
        
        // AB- can donate to AB+ and AB-
        if (this == AB_NEGATIVE && (other == AB_POSITIVE || other == AB_NEGATIVE)) return true
        
        // A+ can donate to A+ and AB+
        if (this == A_POSITIVE && (other == A_POSITIVE || other == AB_POSITIVE)) return true
        
        // B+ can donate to B+ and AB+
        if (this == B_POSITIVE && (other == B_POSITIVE || other == AB_POSITIVE)) return true
        
        return false
    }
}

