package com.notaaiassignmentrdbac.application.project.dto.request

import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest

data class ProjectAddMemberRequest(
    val memberRequests: List<MemberRequest>
)