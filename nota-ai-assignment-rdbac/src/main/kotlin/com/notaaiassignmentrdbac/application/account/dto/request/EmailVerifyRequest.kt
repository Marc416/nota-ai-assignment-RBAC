package com.notaaiassignmentrdbac.application.account.dto.request

import com.notaaiassignmentrdbac.application.aop.ValidEmail
import jakarta.validation.constraints.NotBlank

data class EmailVerifyRequest(
    @field:ValidEmail
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    val email: String
)