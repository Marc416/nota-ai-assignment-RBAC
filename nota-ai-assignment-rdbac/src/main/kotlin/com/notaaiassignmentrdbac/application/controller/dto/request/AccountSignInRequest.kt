package com.notaaiassignmentrdbac.application.controller.dto.request

data class AccountSignInRequest(
    val email: String,
    val tenantKey: String,
    val password: String
)