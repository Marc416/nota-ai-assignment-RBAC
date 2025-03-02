package com.notaaiassignmentrdbac.application.account.dto.request


data class VerifyEmailRequest(
    val email: String,
    val code: String
)