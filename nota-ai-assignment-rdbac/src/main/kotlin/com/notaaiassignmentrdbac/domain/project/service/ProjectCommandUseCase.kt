package com.notaaiassignmentrdbac.domain.project.service

import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest
import com.notaaiassignmentrdbac.domain.project.dto.response.ProjectResponse

interface ProjectCommandUseCase {
    fun addMember(projectId:Long, memberRequests:List<MemberRequest>): ProjectResponse
    fun removeMember(projectId:Long, memberIds:List<Long>): ProjectResponse
    fun updateProject(projectId: Long, newTitle: String): ProjectResponse
    fun deleteProject(projectId: Long): ProjectResponse
    fun createProject(title: String, ownerId: Long, memberRequests: List<MemberRequest>): ProjectResponse
}