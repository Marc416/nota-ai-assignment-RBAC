package com.notaaiassignmentrdbac.application.project.repository

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.project.entity.Project
import com.notaaiassignmentrdbac.domain.project.entity.ProjectStatus
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import com.notaaiassignmentrdbac.util.SliceContent
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
class ProjectRepositoryImpl(
    private val repository: JpaProjectRepository,
) : ProjectRepository {
    override fun save(project: Project): Project {
        return repository.save(project)
    }

    override fun getById(projectId: Long): Project {
        return repository.findById(projectId).orElseThrow {
            throw ApplicationException(code = CodeEnum.FRS_001, message = "없는 프로젝트 입니다.")
        }
    }

    override fun findById(projectId: Long): Project? {
        return repository.findById(projectId).orElse(null)
    }

    override fun getProjects(size: Int, nextCursor: String?): SliceContent<Project> {
        val pageable = PageRequest.ofSize(size)
        val content = if (nextCursor == null) {
            repository.getProjectsFromLatest(status = listOf(ProjectStatus.ACTIVE), pageable = pageable)
        } else {
            repository.getProjectsFromLastId(
                projectId = nextCursor.toLong(),
                status = listOf(ProjectStatus.ACTIVE),
                pageable = pageable
            )
        }
        val id: String? = if (content.isEmpty() || content.size < size)
            null
        else
            content[content.size - 1].id.toString()
        return SliceContent(content, id)
    }
}

interface JpaProjectRepository : JpaRepository<Project, Long> {
    fun save(project: Project): Project

    @Query(
        """
        SELECT p FROM Project p
        WHERE p.status in :status
        ORDER BY p.id DESC
        """
    )
    fun getProjectsFromLatest(status: List<ProjectStatus>, pageable: Pageable): List<Project>

    @Query(
        """
        SELECT p FROM Project p
        WHERE p.id < :projectId and p.status in :status
        ORDER BY p.id DESC
        """
    )
    fun getProjectsFromLastId(projectId: Long, status: List<ProjectStatus>, pageable: Pageable): List<Project>
}