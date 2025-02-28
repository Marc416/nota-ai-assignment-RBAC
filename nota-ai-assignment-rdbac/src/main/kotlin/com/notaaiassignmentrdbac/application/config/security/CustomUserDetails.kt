package com.notaaiassignmentrdbac.application.config.security

import com.notaaiassignmentrdbac.domain.account.entity.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val user: CustomUserDto
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
    }


    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return user.accountId.toString()
    }


}

data class CustomUserDto(
    val accountId: Long,
    val role: Role,
    val tenantKey: String,
)