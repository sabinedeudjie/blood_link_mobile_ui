package com.example.bloodlink.data.model.enums

enum class RequestStatus(val value: String) {
    PENDING("PENDING"),
    REFUSED("REFUSED"),
    ACCEPTED("ACCEPTED"),
    CANCELED("CANCELED");

    companion object {
        fun fromString(value: String): RequestStatus? {
            return entries.find { it.value == value }
        }
    }
}

