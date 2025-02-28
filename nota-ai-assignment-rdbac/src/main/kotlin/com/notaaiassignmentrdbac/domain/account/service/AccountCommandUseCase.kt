package com.notaaiassignmentrdbac.domain.account.service

import com.notaaiassignmentrdbac.application.controller.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.application.controller.dto.response.AccountSignupSuccessResponse
import com.notaaiassignmentrdbac.domain.account.entity.Role

interface AccountCommandUseCase {
    fun changePassword()

    fun signUp(
        email: String,
        password: String,
        tenantKey: String,
        role: Role,
    ): AccountSignupSuccessResponse

    fun signIn(email: String, password: String, tenantKey: String): AccountSignInSuccessResponse
}