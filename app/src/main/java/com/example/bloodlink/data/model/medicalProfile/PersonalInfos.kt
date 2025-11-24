package com.example.bloodlink.data.model.medicalProfile

import com.example.bloodlink.data.model.enums.Gender
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


class PersonalInfos {

    val infosId: UUID? = null

    val birthdate: LocalDate? = null

    val gender: Gender? = null

    val weight = 0f

    val emergencyContact: String? = null

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null

    val age: Int
        get() = birthdate!!.getYear() - LocalDate.now().getYear()
}