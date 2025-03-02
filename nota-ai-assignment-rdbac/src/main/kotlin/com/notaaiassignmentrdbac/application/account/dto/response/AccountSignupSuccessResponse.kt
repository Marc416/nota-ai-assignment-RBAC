package com.notaaiassignmentrdbac.application.account.dto.response

import java.time.LocalDateTime

data class AccountSignupSuccessResponse(
    val id: Long,
    val createdAt: LocalDateTime
)
