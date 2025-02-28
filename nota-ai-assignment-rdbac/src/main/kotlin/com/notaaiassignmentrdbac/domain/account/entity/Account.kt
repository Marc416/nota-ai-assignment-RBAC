package com.notaaiassignmentrdbac.domain.account.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Account(
    val email: String,
    password: String,
    val tenantKey: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
    @Enumerated(EnumType.STRING)
    val status: Status,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    var password: String = password
        private set

    var deletedAt: LocalDateTime? = null

    companion object{
        fun createActiveAccount(email: String, password: String, tenantKey: String, role: Role): Account {
            return Account(email, password, tenantKey, role, Status.ACTIVE)
        }
    }

    fun changePassword(newPassword: String) {
        password = newPassword
    }
}