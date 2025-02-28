package com.notaaiassignmentrdbac.domain.common

import com.notaaiassignmentrdbac.domain.account.dto.AccountJwtPayload

interface JwtTokenProvider {
    fun parseAuthorizationToken(token: String): AccountJwtPayload

    fun validateToken(token: String): Boolean
    fun generateToken(payload: Map<String, Any>, ttl: Int): String
}