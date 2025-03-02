package com.notaaiassignmentrdbac.application.account.dto.request

import com.notaaiassignmentrdbac.domain.account.entity.AccountRole

data class AccountSignupRequest(
    val email: String,
    val password: String,
    val tenantKey: String,
    val role: AccountRole,
)
