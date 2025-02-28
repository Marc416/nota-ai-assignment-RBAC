package com.notaaiassignmentrdbac.domain.account.service

import com.notaaiassignmentrdbac.application.controller.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.application.controller.dto.response.AccountSignupSuccessResponse
import com.notaaiassignmentrdbac.domain.account.dto.AccountJwtPayload
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.Role
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import com.notaaiassignmentrdbac.domain.common.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class AccountCommandService(
    private val accountRepository: AccountRepository,
    private val jwtTokenProvider: JwtTokenProvider
) : AccountCommandUseCase {
    override fun signUp(
        email: String,
        password: String,
        tenantKey: String,
        role: Role,
    ): AccountSignupSuccessResponse {
        val account = Account.createActiveAccount(email, password, tenantKey, role)
        val createdAccount = accountRepository.save(account)
        return AccountSignupSuccessResponse(createdAccount.id, createdAccount.createdAt)
    }

    override fun signIn(email: String, tenantKey: String, password: String): AccountSignInSuccessResponse {
        val account = accountRepository.findByEmailAndTenantKeyAndPassword(email, password, tenantKey)
        val accountPayload = AccountJwtPayload(account.id, account.role.name).toMap()
        val token = jwtTokenProvider.generateToken(accountPayload, 3000000)
        return AccountSignInSuccessResponse(token)
    }

    override fun changePassword(userId:Long, newPassword: String) {
        val account = accountRepository.findByUserId(userId)
        account.changePassword(newPassword)
        accountRepository.save(account)
    }
}