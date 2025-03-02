package com.notaaiassignmentrdbac.domain.project.service

import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectViewResponse
import com.notaaiassignmentrdbac.util.SliceContent

interface ProjectQueryUseCase {
    fun getProjects(size: Int, nextCursor: String?): SliceContent<ProjectViewResponse>
}