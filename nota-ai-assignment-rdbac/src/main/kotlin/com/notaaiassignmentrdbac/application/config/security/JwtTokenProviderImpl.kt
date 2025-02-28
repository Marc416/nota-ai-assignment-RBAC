package com.notaaiassignmentrdbac.application.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.notaaiassignmentrdbac.domain.common.JwtTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*


@Service
class JwtTokenProviderImpl(
    @Value("\${jwt.secret-key}")
    private val secretKey: String
) : JwtTokenProvider {

    override fun generateToken(payload: Map<String, Any>, ttl:Int): String {
        val algorithm: Algorithm = Algorithm.HMAC256(secretKey)

        return JWT.create()
            .withPayload(payload)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + ttl))
            .sign(algorithm)
    }

    override fun parseToken(token: String): Map<String, Any> {
        val decodedJWT = JWT.decode(token)
//        val userId = decodedJWT.getClaim("userId").asLong()
//        val role = decodedJWT.getClaim("role").asString()
//
//        return mapOf(
//            "userId" to userId,
//            "role" to role,
//        )
        return decodedJWT.claims.mapValues { (_, claim) ->
            when {
                claim.asBoolean() != null -> claim.asBoolean()
                claim.asInt() != null -> claim.asInt()
                claim.asLong() != null -> claim.asLong()
                claim.asDouble() != null -> claim.asDouble()
                claim.asString() != null -> claim.asString()
                claim.asDate() != null -> claim.asDate()
                else -> throw IllegalArgumentException("Unsupported claim type")
            }
        }
    }

    override fun validateToken(token: String): Boolean {
        return try {
            JWT.decode(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}