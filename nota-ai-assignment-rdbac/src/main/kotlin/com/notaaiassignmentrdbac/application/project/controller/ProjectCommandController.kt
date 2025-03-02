package com.notaaiassignmentrdbac.application.project.controller

import com.notaaiassignmentrdbac.application.aop.CheckProjectRole
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectAddMemberRequest
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectCreateRequest
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectUpdateRequest
import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectResponse
import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole
import com.notaaiassignmentrdbac.domain.project.service.ProjectCommandUseCase
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project")
class ProjectCommandController(
    private val projectCommandUseCase: ProjectCommandUseCase
) {
    @CheckProjectRole(ProjectRole.PROJECT_OWNER)
    @PostMapping("")
    fun createProject(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: ProjectCreateRequest
    ): HttpApiResponse<ProjectResponse> {
        val projectResponse = projectCommandUseCase.createProject(
            title = request.title,
            ownerId = userDetails.user.accountId,
            memberRequests = request.memberRequests
        )
        return HttpApiResponse.of(projectResponse)
    }

    @CheckProjectRole(ProjectRole.EDITOR)
    @PutMapping("/{projectId}")
    fun updateProject(
        @PathVariable projectId: Long,
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: ProjectUpdateRequest
    ): HttpApiResponse<ProjectResponse> {
        val response = projectCommandUseCase.updateProject(projectId, request.title)
        return HttpApiResponse.of(response)
    }

    @CheckProjectRole(ProjectRole.PROJECT_OWNER)
    @DeleteMapping("/{projectId}")
    fun deleteProject(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable projectId: Long,
    ): HttpApiResponse<ProjectResponse> {
        val response = projectCommandUseCase.deleteProject(
            projectId = projectId,
        )
        return HttpApiResponse.of(response)
    }

    @CheckProjectRole(ProjectRole.PROJECT_OWNER)
    @PostMapping("/{projectId}/members")
    fun addMember(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable projectId: Long,
        @RequestBody request: ProjectAddMemberRequest
    ): HttpApiResponse<ProjectResponse> {
        val response = projectCommandUseCase.addMember(
            projectId = projectId,
            memberRequests = request.memberRequests
        )
        return HttpApiResponse.of(response)
    }

    @CheckProjectRole(ProjectRole.PROJECT_OWNER)
    @DeleteMapping("/{projectId}/members")
    fun removeMember(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable projectId: Long,
        @RequestBody request: ProjectAddMemberRequest
    ): HttpApiResponse<ProjectResponse> {
        val response = projectCommandUseCase.removeMember(
            projectId = projectId,
            memberRequests = request.memberRequests
        )
        return HttpApiResponse.of(response)
    }
}