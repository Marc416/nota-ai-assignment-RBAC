package com.notaaiassignmentrdbac.domain.project.repository

import com.notaaiassignmentrdbac.domain.project.entity.ProjectMember

interface ProjectMemberRepository {
    fun saveAll(members: List<ProjectMember>):List<ProjectMember>

    fun updateAll(members: List<ProjectMember>):List<ProjectMember>

    fun findByAccountIdAndProjectId(accountId: Long, projectId: Long): ProjectMember?
}