package com.notaaiassignmentrdbac.application.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.OutputStream


@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = "application/json"
        response.status = HttpStatus.UNAUTHORIZED.value()
        val errorResponse = HttpApiResponse.fromExceptionMessage(
            code = CodeEnum.FRS_002, // 인증 실패 코드
            message = "유효한 인증 자격 증명이 필요합니다."
        )

        val out: OutputStream = response.outputStream
        objectMapper.writeValue(out, errorResponse)
        out.flush()
    }
}