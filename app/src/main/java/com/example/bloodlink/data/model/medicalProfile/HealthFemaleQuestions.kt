package com.example.bloodlink.data.model.medicalProfile

import com.example.bloodlink.data.model.medicalProfile.HealthQuestions
import java.time.LocalDate

class HealthFemaleQuestions : HealthQuestions() {
    val isPregnant = false

    val isBreastFeeding = false

    val hasHeavyMenstrualFlow = false

    /*    @Column(name ="has_recent_child_birth")
    private boolean hasRecentChildBirth;*/
    val isChildBirthWithinLast3Months = false

    val lastMenstrualPeriod: LocalDate? = null

    //TODO: Write a method which returns the number of days between the last menstrual period date and the date of the day; And complete the function validateAnswers()
    fun validateAnswers(): Boolean {
        return isPregnant || isBreastFeeding || isChildBirthWithinLast3Months || hasHeavyMenstrualFlow
    }
}