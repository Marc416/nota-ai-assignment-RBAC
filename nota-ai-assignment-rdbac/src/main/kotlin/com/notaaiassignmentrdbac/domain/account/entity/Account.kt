package com.notaaiassignmentrdbac.domain.account.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Account(
    val email: String,
    password: String,
    val tenantKey: String,
    status: AccountStatus,
    @Enumerated(EnumType.STRING)
    val role: AccountRole,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var password: String = password
        private set

    @Enumerated(EnumType.STRING)
    var status: AccountStatus = status
        private set
    var deletedAt: LocalDateTime? = null
        private set

    companion object {
        fun createActiveAccount(email: String, password: String, tenantKey: String, role: AccountRole): Account {
            return Account(
                email = email,
                password = password,
                tenantKey = tenantKey,
                status = AccountStatus.ACTIVE,
                role = role,
            )
        }
    }

    fun changePassword(newPassword: String) {
        password = newPassword
    }

    fun delete() {
        status = AccountStatus.INACTIVE
        deletedAt = LocalDateTime.now()
    }
}