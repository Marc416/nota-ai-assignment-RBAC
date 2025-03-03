package com.notaaiassignmentrdbac.domain.account.service

import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignupSuccessResponse
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole

interface AccountCommandUseCase {

    fun signUp(
        email: String,
        password: String,
        tenantKey: String,
        role: AccountRole,
    ): AccountSignupSuccessResponse

    fun signIn(email: String, tenantKey: String, password: String,): AccountSignInSuccessResponse
    fun changePassword(userId: Long, newPassword: String)
    fun deleteAccount(accountId: Long)
}