package com.notaaiassignmentrdbac.domain.account.entity

enum class Role(val description: String) {
    ADMIN("관리자"),
    PROJECT_OWNER("프로젝트 소유자"),
    EDITOR("편집자"),
    VIEWER("뷰어"),
}