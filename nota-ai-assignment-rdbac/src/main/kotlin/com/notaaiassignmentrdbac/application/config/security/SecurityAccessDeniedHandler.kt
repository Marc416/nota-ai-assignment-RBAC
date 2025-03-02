package com.notaaiassignmentrdbac.application.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.io.OutputStream

@Component
class SecurityAccessDeniedHandler(
    private val objectMapper: ObjectMapper // JSON 변환기
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = "application/json"
        response.status = HttpStatus.FORBIDDEN.value()

        val errorResponse = HttpApiResponse.fromExceptionMessage(
            code = CodeEnum.FRS_002,
            message = accessDeniedException.message ?: "접근 권한이 없습니다."
        )

        val out: OutputStream = response.outputStream
        objectMapper.writeValue(out, errorResponse)
        out.flush()
    }

}