package com.example.bloodlink.data.model.appl

import com.example.bloodlink.data.model.enums.AlertResponse
import java.time.LocalDateTime
import java.util.UUID


class DonorResponse {

    val id: UUID? = null

    val response = AlertResponse

    val createdAt: LocalDateTime? = null

    val updatedAt: LocalDateTime? = null /*    @Column(name = "content")
    private boolean content;*/
}