package com.notaaiassignmentrdbac.application.account.controller

import com.notaaiassignmentrdbac.application.account.dto.request.*
import com.notaaiassignmentrdbac.application.aop.CheckRole
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignupSuccessResponse
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.service.AccountCommandUseCase
import com.notaaiassignmentrdbac.domain.account.service.EmailVerifyUseCase
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountCommandController(
    private val emailVerifyUseCase: EmailVerifyUseCase,
    private val accountCommandUseCase: AccountCommandUseCase,
) {
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody requestBody: AccountSignupRequest
    ) :HttpApiResponse<AccountSignupSuccessResponse> {
        val response = accountCommandUseCase.signUp(
            email = requestBody.email,
            password = requestBody.password,
            tenantKey = requestBody.tenantKey,
            role = requestBody.role
        )
        return HttpApiResponse.of(response)
    }

    @PostMapping("/signin")
    fun signIn(
        @Valid @RequestBody requestBody: AccountSignInRequest
    ): HttpApiResponse<AccountSignInSuccessResponse> {
        val response = accountCommandUseCase.signIn(
            email = requestBody.email,
            tenantKey = requestBody.tenantKey,
            password = requestBody.password
        )
        return HttpApiResponse.of(response)
    }

    /**
     * 인증코드 이메일 전송
     */
    @PostMapping("/verify/email")
    fun sendVerifyCodeToEmail(
        @Valid @RequestBody requestBody: EmailVerifyRequest
    ): HttpApiResponse<Unit> {
        emailVerifyUseCase.sendVerifyCodeToEmail(requestBody.email)
        return HttpApiResponse.ok()
    }

    /**
     * 이메일로 받은 인증코드 확인
     */
    @PostMapping("/verify/email/code")
    fun verifyEmailCode(
        @Valid @RequestBody requestBody: VerifyEmailRequest,
    ): HttpApiResponse<Unit> {
        emailVerifyUseCase.verifyEmailCode(requestBody.email, requestBody.code)
        return HttpApiResponse.ok()
    }

    @PatchMapping("/password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): HttpApiResponse<Unit> {
        accountCommandUseCase.changePassword(userDetails.user.accountId, request.newPassword)
        return HttpApiResponse.ok()
    }

    @CheckRole(AccountRole.ADMIN)
    @DeleteMapping("/{accountId}")
    fun deleteAccount(
        @PathVariable accountId: Long,
    ): HttpApiResponse<Unit> {
        accountCommandUseCase.deleteAccount(accountId)
        return HttpApiResponse.ok()
    }
}