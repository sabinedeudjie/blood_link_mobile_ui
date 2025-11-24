package com.example.bloodlink.data.model.metiers

import com.example.bloodlink.data.model.appl.DonationRequest
import com.example.bloodlink.data.model.appl.Notification
import com.example.bloodlink.data.model.enums.BloodType
import com.example.bloodlink.data.model.medicalProfile.MedicalProfile
import java.time.LocalDate


class Donor : Person() {

    val bloodType: BloodType? = null

    val lastDonationDate: LocalDate? = null

    val notifications: MutableList<Notification?>? = null

    val donationRequests: MutableList<DonationRequest?>? = null

    val medicalProfile: MedicalProfile? = null

    val affiliatedDonorBanks: MutableSet<BloodBank?> = HashSet<BloodBank?>()

    fun affiliateNewBank(bloodBank: BloodBank?) {
        this.affiliatedDonorBanks.add(bloodBank)
    }

    fun removeAffiliateBank(bloodBank: BloodBank?) {
        this.affiliatedDonorBanks.remove(bloodBank)
    }
}

