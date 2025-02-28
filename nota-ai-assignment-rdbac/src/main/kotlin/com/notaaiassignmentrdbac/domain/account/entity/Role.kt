package com.notaaiassignmentrdbac.domain.account.entity

enum class Role(val inheritedRoles: Set<Role>, val description: String) {
    VIEWER(emptySet(), "뷰어"),
    EDITOR(setOf(VIEWER), "편집자"),
    PROJECT_OWNER(setOf(EDITOR), "프로젝트 소유자"),
    ADMIN(setOf(PROJECT_OWNER), "관리자");

    fun getAllRoles(): Set<Role> {
        val roles = mutableSetOf(this)
        inheritedRoles.forEach { roles.addAll(it.getAllRoles()) }
        return roles
    }
}