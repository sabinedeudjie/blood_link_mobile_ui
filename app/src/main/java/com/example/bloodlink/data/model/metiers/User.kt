package com.example.bloodlink.data.model.metiers

import com.example.bloodlink.data.model.enums.UserRole
import java.time.LocalDateTime
import java.util.UUID


open class User{

    val id: UUID? = null

    val username: String? = null

    val phoneNumber: String? = null

    val password: String? = null

    var userRole: UserRole? = null

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null


    val isAccountNonExpired: Boolean
        get() = true
    /*UserDetails.super.isAccountNonExpired()*/

    val isAccountNonLocked: Boolean
        get() = true
    /*UserDetails.super.isAccountNonLocked()*/

    val isCredentialsNonExpired: Boolean
        get() = true
    /*UserDetails.super.isCredentialsNonExpired()*/

    val isEnabled: Boolean
        get() = true
    /*UserDetails.super.isEnabled()*/
}


