package com.notaaiassignmentrdbac.application.controller.dto.request


data class VerifyEmailRequest(
    val email: String,
    val code: String
)