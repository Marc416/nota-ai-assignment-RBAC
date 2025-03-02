package com.notaaiassignmentrdbac.application.project.dto.request

import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest

data class ProjectCreateRequest(
    val title: String,
    val memberRequests: List<MemberRequest> = emptyList()
)