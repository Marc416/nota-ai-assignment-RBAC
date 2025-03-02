package com.notaaiassignmentrdbac.application.account.controller

import com.notaaiassignmentrdbac.application.aop.CheckRole
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.account.dto.request.AccountSignInRequest
import com.notaaiassignmentrdbac.application.account.dto.request.AccountSignupRequest
import com.notaaiassignmentrdbac.application.account.dto.request.ChangePasswordRequest
import com.notaaiassignmentrdbac.application.account.dto.request.VerifyEmailRequest
import com.notaaiassignmentrdbac.application.account.dto.response.AccountSignInSuccessResponse
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.service.AccountCommandUseCase
import com.notaaiassignmentrdbac.domain.account.service.EmailVerifyUseCase
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
        @RequestBody requestBody: AccountSignupRequest
    ) {
        accountCommandUseCase.signUp(
            email = requestBody.email,
            password = requestBody.password,
            tenantKey = requestBody.tenantKey,
            role = requestBody.role
        )
    }

    @PostMapping("/signin")
    fun signIn(
        @RequestBody requestBody: AccountSignInRequest
    ): HttpApiResponse<AccountSignInSuccessResponse> {
        val response = accountCommandUseCase.signIn(
            email = requestBody.email,
            password = requestBody.password,
            tenantKey = requestBody.tenantKey
        )
        return HttpApiResponse.of(response)
    }

    /**
     * 인증코드 이메일 전송
     */
    @PostMapping("/verify/email")
    fun sendVerifyCodeToEmail(
        @RequestBody email: String
    ): HttpApiResponse<Unit> {
        emailVerifyUseCase.sendVerifyCodeToEmail(email)
        return HttpApiResponse.ok()
    }

    /**
     * 이메일로 받은 인증코드 확인
     */
    @PostMapping("/verify/email/code")
    fun verifyEmailCode(
        @RequestBody requestBody: VerifyEmailRequest,
    ): HttpApiResponse<Unit> {
        emailVerifyUseCase.verifyEmailCode(requestBody.email, requestBody.code)
        return HttpApiResponse.ok()
    }

    @PatchMapping("/password")
    fun changePassword(
        @RequestBody request: ChangePasswordRequest,
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