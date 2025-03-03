package com.notaaiassignmentrdbac.application.account.dto.request

import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    val newPassword:String
)
