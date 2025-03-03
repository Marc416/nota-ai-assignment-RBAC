package com.notaaiassignmentrdbac.application.account.dto.request

import com.notaaiassignmentrdbac.application.aop.ValidEmail
import jakarta.validation.constraints.NotBlank

data class AccountSignInRequest(
    @field:ValidEmail
    val email: String,
    @field:NotBlank(message = "테넌트는 필수 입력값입니다.")
    val tenantKey: String,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    val password: String
)