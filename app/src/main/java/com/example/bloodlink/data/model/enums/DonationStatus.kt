package com.example.bloodlink.data.model.enums

enum class DonationStatus(val value: String) {
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED");

    companion object {
        fun fromString(value: String): DonationStatus? {
            return entries.find { it.value == value }
        }
    }
}

