package com.notaaiassignmentrdbac.domain.project.entity

enum class ProjectRole(val inheritedRoles: Set<ProjectRole>, val description: String) {
    VIEWER(emptySet(), "뷰어"),
    EDITOR(setOf(VIEWER), "편집자"),
    PROJECT_OWNER(setOf(EDITOR), "프로젝트 소유자");

    fun getAllRoles(): Set<ProjectRole> {
        val roles = mutableSetOf(this)
        inheritedRoles.forEach { roles.addAll(it.getAllRoles()) }
        return roles
    }
}
