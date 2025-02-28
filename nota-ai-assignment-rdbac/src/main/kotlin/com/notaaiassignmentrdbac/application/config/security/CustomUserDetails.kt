package com.notaaiassignmentrdbac.application.config.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val user: CustomUserDto
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
    }


    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return user.userId.toString()
    }


}

data class CustomUserDto(
    val userId: Long,
    val role: String,
    val tenantKey: String,
)