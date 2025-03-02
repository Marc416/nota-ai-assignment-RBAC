package com.notaaiassignmentrdbac.application.project.controller

import com.notaaiassignmentrdbac.application.aop.CheckProjectRole
import com.notaaiassignmentrdbac.application.common.httpresponse.HttpApiResponse
import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectViewResponse
import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole
import com.notaaiassignmentrdbac.domain.project.service.ProjectQueryUseCase
import com.notaaiassignmentrdbac.util.SliceContent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectQueryController(
    private val projectQueryUseCase: ProjectQueryUseCase
) {
    @CheckProjectRole(ProjectRole.VIEWER)
    @GetMapping("")
    fun getProjects(
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam nextCursor: String? = null,
    ): HttpApiResponse<SliceContent<ProjectViewResponse>> {
        val response = projectQueryUseCase.getProjects(size = size, nextCursor = nextCursor)
        return HttpApiResponse.of(response)
    }
}