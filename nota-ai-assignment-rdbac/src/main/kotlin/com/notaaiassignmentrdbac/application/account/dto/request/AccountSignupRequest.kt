package com.notaaiassignmentrdbac.application.account.dto.request

import com.notaaiassignmentrdbac.application.aop.ValidEmail
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import jakarta.validation.constraints.NotBlank

data class AccountSignupRequest(
    @field:ValidEmail
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    val password: String,
    @field:NotBlank(message = "테넌트는 필수 입력값입니다.")
    val tenantKey: String,
    val role: AccountRole,
)
