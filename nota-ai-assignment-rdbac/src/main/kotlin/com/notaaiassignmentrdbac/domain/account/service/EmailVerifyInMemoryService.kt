package com.notaaiassignmentrdbac.domain.account.service

import com.notaaiassignmentrdbac.domain.account.port.out.EmailServicePort
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val logger = KotlinLogging.logger {}

@Service
class EmailVerifyInMemoryService(
    private val emailServicePort: EmailServicePort
):EmailVerifyUseCase {
    private val verificationCodes: MutableMap<String, String> = ConcurrentHashMap()

    override fun sendVerifyCodeToEmail(email: String): String {
        val code = generateVerificationCode()
        verificationCodes[email] = code
        emailServicePort.send(email, code)
        logger.info {
            "Verification code sent to $email: $code"
        }
        return code
    }

    override fun verifyEmailCode(email: String, code: String): Boolean {
        return verificationCodes.containsKey(email) && verificationCodes[email] == code
    }

    private fun generateVerificationCode(): String {
        return java.lang.String.valueOf(Random().nextInt(900000) + 100000) // 6자리 숫자 코드 생성
    }
}