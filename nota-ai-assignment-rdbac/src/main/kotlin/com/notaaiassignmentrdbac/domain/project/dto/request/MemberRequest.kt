package com.notaaiassignmentrdbac.domain.project.dto.request

import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole

data class MemberRequest(
    val accountId: Long,
    val role: ProjectRole
)
