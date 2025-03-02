package com.notaaiassignmentrdbac.domain.project.dto.response

data class ProjectViewResponse (
    val projectId: Long,
    val title: String,
    val projectOwner: Long
)