package com.example.bloodlink.data.model.metiers

import com.example.bloodlink.data.model.appl.BloodRequest
import java.util.UUID


class Doctor : Person() {
    val licenseNumber: UUID? = null

    val hospitalName: String? = null

    val speciality: String? = null

    //    @JsonIgnore
    val bloodRequests: MutableList<BloodRequest?> = ArrayList<BloodRequest?>()

    val affiliatedDoctorBanks: MutableSet<BloodBank?> = HashSet<BloodBank?>()
}

