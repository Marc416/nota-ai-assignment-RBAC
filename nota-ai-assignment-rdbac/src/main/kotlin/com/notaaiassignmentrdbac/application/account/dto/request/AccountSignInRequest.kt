package com.notaaiassignmentrdbac.application.account.dto.request

data class AccountSignInRequest(
    val email: String,
    val tenantKey: String,
    val password: String
)