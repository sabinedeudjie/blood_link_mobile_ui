package com.example.bloodlink.data.model.dtos.requests


@JvmRecord
data class ProfileRequest(
    val infosRequest: InfosRequest?,
    val SignsRequest: SignsRequest?,
    val questionsRequest: QuestionsRequest?
)