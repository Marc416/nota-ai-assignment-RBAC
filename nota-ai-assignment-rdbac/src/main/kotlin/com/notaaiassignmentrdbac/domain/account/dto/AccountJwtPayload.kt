package com.notaaiassignmentrdbac.domain.account.dto

data class AccountJwtPayload(
    val userId: Long,
    val role: String,
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "role" to role
        )
    }
}
