package com.example.bloodlink.data.model.dtos.requests

import java.time.LocalDate


@JvmRecord
data class QuestionsRequest( //General health questions
    val hasTattoosWithinLast6Months: Boolean?,
    val hasSurgeryWithinLast6_12Months: Boolean?,
    val hasChronicalIllness: Boolean?,
    val hasTravelledWithinLast3Months: Boolean?,
    val hasPiercingWithinLast7Months: Boolean?,
    val isOnMedication: Boolean?,


    // specific questions to female

    val isPregnant: Boolean?,
    val isBreastFeeding: Boolean?,
    val isChildBirthWithinLast3Months: Boolean?,
    val hasHeavyMenstrualFlow: Boolean?,
    val lastMenstrualPeriod: LocalDate?
)