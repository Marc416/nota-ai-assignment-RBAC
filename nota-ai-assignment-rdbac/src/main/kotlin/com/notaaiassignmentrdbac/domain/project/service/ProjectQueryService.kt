package com.notaaiassignmentrdbac.domain.project.service

import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectViewResponse
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import com.notaaiassignmentrdbac.util.SliceContent
import org.springframework.stereotype.Service

@Service
class ProjectQueryService(
    private val projectRepository: ProjectRepository
) : ProjectQueryUseCase {
    override fun getProjects(size: Int, nextCursor: String?): SliceContent<ProjectViewResponse> {
        val result = projectRepository.getProjects(size = size, nextCursor = nextCursor)
        val content = result.content.map {
            ProjectViewResponse(
                projectId = it.id,
                title = it.title,
                projectOwner = it.ownerId
            )
        }
        return SliceContent(content = content, nextCursor = result.nextCursor)
    }
}