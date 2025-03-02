package com.notaaiassignmentrdbac.domain.account.entity

import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.jvm.Transient

@Entity
class Account(
    val email: String,
    password: String,
    val tenantKey: String,
    status: AccountStatus,
    @Enumerated(EnumType.STRING)
    val role: AccountRole,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Transient
    val passwordEncoder: PasswordEncoder
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
        fun createActiveAccount(
            email: String,
            password: String,
            tenantKey: String,
            role: AccountRole,
            passwordEncoder: PasswordEncoder
        ): Account {
            return Account(
                email = email,
                password = passwordEncoder.encode(password),
                tenantKey = tenantKey,
                status = AccountStatus.ACTIVE,
                role = role,
                passwordEncoder=passwordEncoder
            )
        }
    }

    fun changePassword(newPassword: String) {
        password = passwordEncoder.encode(newPassword)
    }

    fun isPasswordValid(inputPassword: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(inputPassword, this.password)
    }

    fun delete() {
        status = AccountStatus.INACTIVE
        deletedAt = LocalDateTime.now()
    }
}