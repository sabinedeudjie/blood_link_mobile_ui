package com.example.bloodlink.data.model.metiers

import com.example.bloodlink.data.model.appl.BloodBankStock
import java.util.UUID

class BloodBank : User() {
    val bloodBankName: String? = null

    val licenseNumber: UUID? = null

    val address: String? = null

    val stock: BloodBankStock? = null

    //
    //    @ManyToMany(mappedBy = "affiliatedDoctorBanks")
    //    private Set<Doctor> affiliatedDoctors = new HashSet<>();
    val affiliatedDonors: MutableSet<Donor?> = HashSet<Donor?>()
}
