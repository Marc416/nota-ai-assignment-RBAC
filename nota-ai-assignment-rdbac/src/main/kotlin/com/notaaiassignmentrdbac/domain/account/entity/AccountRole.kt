package com.notaaiassignmentrdbac.domain.account.entity

enum class AccountRole(val inheritedRoles: Set<AccountRole>, val description: String) {
    USER(emptySet(), "일반 사용자"),
    ADMIN(setOf(USER), "관리자");

    fun getAllRoles(): Set<AccountRole> {
        val roles = mutableSetOf(this)
        inheritedRoles.forEach { roles.addAll(it.getAllRoles()) }
        return roles
    }
}