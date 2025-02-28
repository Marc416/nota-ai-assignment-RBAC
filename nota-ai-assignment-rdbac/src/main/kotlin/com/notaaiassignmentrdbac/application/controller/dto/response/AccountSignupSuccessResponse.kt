package com.notaaiassignmentrdbac.application.controller.dto.response

import java.time.LocalDateTime

data class AccountSignupSuccessResponse(
    val id: Long,
    val createdAt: LocalDateTime
)
