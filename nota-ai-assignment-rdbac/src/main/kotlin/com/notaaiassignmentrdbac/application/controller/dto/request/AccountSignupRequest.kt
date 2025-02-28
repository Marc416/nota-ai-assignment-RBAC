package com.notaaiassignmentrdbac.application.controller.dto.request

import com.notaaiassignmentrdbac.domain.account.entity.Role
import com.notaaiassignmentrdbac.domain.account.entity.Status

data class AccountSignupRequest(
    val email: String,
    val password: String,
    val tenantKey: String,
    val role: Role,
)
