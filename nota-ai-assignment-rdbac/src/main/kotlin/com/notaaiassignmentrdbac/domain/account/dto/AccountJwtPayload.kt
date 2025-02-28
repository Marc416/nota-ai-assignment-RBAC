package com.notaaiassignmentrdbac.domain.account.dto

data class AccountJwtPayload(
    val accountId: Long,
    val tenantKey: String,
    val role: String,
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "accountId" to accountId,
            "tenantKey" to tenantKey,
            "role" to role
        )
    }
}
