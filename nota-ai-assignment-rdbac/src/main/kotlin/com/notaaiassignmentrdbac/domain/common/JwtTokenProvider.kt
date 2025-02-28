package com.notaaiassignmentrdbac.domain.common

interface JwtTokenProvider {
    fun parseToken(token: String): Map<String, Any>

    fun validateToken(token: String): Boolean
    fun generateToken(payload: Map<String, Any>, ttl: Int): String
}