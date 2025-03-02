package com.notaaiassignmentrdbac.application.account.port.out

import com.notaaiassignmentrdbac.domain.account.port.out.EmailServicePort
import org.springframework.stereotype.Component

/**
 * 이메일 전송을 위한 Port의 Stub 구현체, 실제로 이메일을 전송하지 않고 성공으로 가정한다.
 */
@Component
class EmailServicePortStubImpl : EmailServicePort {
    override fun send(email: String, code: String): Boolean {
        // 이메일 전송 로직
        return true
    }
}