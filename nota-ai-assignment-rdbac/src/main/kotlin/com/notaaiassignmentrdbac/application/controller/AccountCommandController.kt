package com.notaaiassignmentrdbac.application.controller

import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import com.notaaiassignmentrdbac.application.controller.dto.request.VerifyEmailRequest
import com.notaaiassignmentrdbac.domain.account.service.EmailVerifyUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountCommandController(
    private val emailVerifyUseCase: EmailVerifyUseCase
) {
    @PostMapping("/signup")
    fun signUp() {

    }

    @PostMapping("/signin")
    fun signIn() {

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

    /**
     * 비밀번호 변경
     */
    @PostMapping("/password/change")
    fun changePassword() {

    }
}