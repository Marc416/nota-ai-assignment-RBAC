package com.notaaiassignmentrdbac.domain.account.dto

data class AccountJwtPayload(
    val userId: Long,
    val tenantKey: String,
    val role: String,
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "tenantKey" to tenantKey,
            "role" to role
        )
    }
}
