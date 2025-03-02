package com.notaaiassignmentrdbac.domain.project.repository

import com.notaaiassignmentrdbac.domain.project.entity.Project
import com.notaaiassignmentrdbac.util.SliceContent

interface ProjectRepository {
    fun save(project: Project): Project
    fun getById(projectId: Long): Project
    fun findById(projectId: Long): Project?

    fun getProjects(size:Int, nextCursor:String?): SliceContent<Project>
}