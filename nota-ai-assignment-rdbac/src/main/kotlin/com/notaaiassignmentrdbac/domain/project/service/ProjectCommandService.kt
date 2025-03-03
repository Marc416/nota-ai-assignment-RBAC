package com.notaaiassignmentrdbac.domain.project.service

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest
import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectResponse
import com.notaaiassignmentrdbac.domain.project.entity.Project
import com.notaaiassignmentrdbac.domain.project.entity.ProjectMember
import com.notaaiassignmentrdbac.domain.project.repository.ProjectMemberRepository
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectCommandService(
    private val projectRepository: ProjectRepository,
    private val projectMemberRepository: ProjectMemberRepository,
) : ProjectCommandUseCase {
    @Transactional
    override fun createProject(title: String, ownerId: Long, memberRequests: List<MemberRequest>): ProjectResponse {
        val createdProject =
            Project.createActiveProject(title = title, ownerId = ownerId, memberRequests = memberRequests)
        val savedProject = projectRepository.save(createdProject)
        val members = memberRequests.map { memberRequest ->
            ProjectMember(memberRequest.accountId, savedProject, role = memberRequest.role)
        }
        projectMemberRepository.saveAll(members)
        return ProjectResponse(projectId = savedProject.id)
    }

    override fun addMember(projectId: Long, memberRequests: List<MemberRequest>): ProjectResponse {
        val project = projectRepository.getById(projectId)
        memberRequests.forEach { memberRequest ->
            project.addMember(memberRequest.accountId, memberRequest.role)
        }
        val members = memberRequests.map { memberRequest ->
            ProjectMember(memberRequest.accountId, project, role = memberRequest.role)
        }
        projectMemberRepository.saveAll(members)
        return ProjectResponse(projectId = projectId)
    }

    override fun removeMember(projectId: Long, memberIds: List<Long>): ProjectResponse {
        val project = projectRepository.getById(projectId)
        val memberMap = project.members.associateBy { it.accountId }
        val members = memberIds.map { memberRequest ->
            memberMap[memberRequest] ?: throw ApplicationException(
                code=CodeEnum.FRS_001,
                message = "Id: ${memberRequest} 는 멤버가 아닙니다."
            )
        }

        members.forEach { project.removeMember(it) }
        projectMemberRepository.updateAll(members)

        return ProjectResponse(projectId = projectId)
    }

    override fun updateProject(projectId: Long, newTitle: String): ProjectResponse {
        val project = projectRepository.getById(projectId)
        project.updateTitle(newTitle)
        projectRepository.save(project)
        return ProjectResponse(projectId)
    }

    override fun deleteProject(projectId: Long): ProjectResponse {
        val project = projectRepository.getById(projectId)
        project.delete()
        projectRepository.save(project)
        return ProjectResponse(projectId)
    }
}