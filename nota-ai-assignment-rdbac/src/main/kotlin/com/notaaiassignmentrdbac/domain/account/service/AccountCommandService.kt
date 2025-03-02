package com.notaaiassignmentrdbac.domain.account.service

import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignupSuccessResponse
import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.dto.AccountJwtPayload
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import com.notaaiassignmentrdbac.domain.common.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountCommandService(
    private val accountRepository: AccountRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) : AccountCommandUseCase {
    override fun signUp(
        email: String,
        password: String,
        tenantKey: String,
        role: AccountRole,
    ): AccountSignupSuccessResponse {
        val account = Account.createActiveAccount(email, password, tenantKey, role, passwordEncoder)
        val createdAccount = accountRepository.save(account)
        return AccountSignupSuccessResponse(createdAccount.id, createdAccount.createdAt)
    }

    override fun signIn(email: String, tenantKey: String, password: String): AccountSignInSuccessResponse {
        val account = accountRepository.findByEmailAndTenantKey(
            email = email,
            tenantKey = tenantKey
        )

        if (!account.isPasswordValid(password, passwordEncoder)) {
            throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "비밀번호가 맞지 않습니다."
            )
        }

        val accountPayload = AccountJwtPayload(
            accountId = account.id,
            tenantKey = account.tenantKey,
            role = account.role.name
        ).toMap()
        val token = jwtTokenProvider.generateToken(accountPayload, 1000 * 60 * 60 * 24)
        return AccountSignInSuccessResponse(token)
    }

    override fun changePassword(accountId: Long, newPassword: String) {
        val account = accountRepository.findByAccountId(accountId)
        account.changePassword(newPassword)
        accountRepository.save(account)
    }

    override fun deleteAccount(accountId: Long) {
        val account = accountRepository.findByAccountId(accountId)
        account.delete()
        accountRepository.save(account)
    }
}