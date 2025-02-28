package com.notaaiassignmentrdbac.domain.account.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Account(
    val email: String,
    password: String,
    val tenantKey: String,
    status: Status,
    @Enumerated(EnumType.STRING)
    val role: Role,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var password: String = password
        private set

    @Enumerated(EnumType.STRING)
    var status: Status = status
        private set
    var deletedAt: LocalDateTime? = null

    companion object {
        fun createActiveAccount(email: String, password: String, tenantKey: String, role: Role): Account {
            return Account(
                email = email,
                password = password,
                tenantKey = tenantKey,
                status = Status.ACTIVE,
                role = role,
            )
        }
    }

    fun changePassword(newPassword: String) {
        password = newPassword
    }

    fun delete() {
        status = Status.INACTIVE
        deletedAt = LocalDateTime.now()
    }
}