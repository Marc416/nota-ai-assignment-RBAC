package com.notaaiassignmentrdbac.application.project.repository

import com.notaaiassignmentrdbac.domain.project.entity.ProjectMember
import com.notaaiassignmentrdbac.domain.project.repository.ProjectMemberRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ProjectMemberRepositoryImpl(
    private val repository: JpaProjectMemberRepository,
    private val jdbcTemplate: JdbcTemplate
):ProjectMemberRepository {
    override fun saveAll(members: List<ProjectMember>):List<ProjectMember> {
        updateAlreadyExistMembers(members)

        addNewMembers(members)
        return members
    }

    private fun updateAlreadyExistMembers(members: List<ProjectMember>) {
        val sqlUpdate = """
        UPDATE project_member
        SET role = ?, deleted_at = NULL
        WHERE account_id = ? AND project_id = ?
        """
        val batchUpdateArgs = members.map { member ->
            arrayOf(
                member.role.name,
                member.accountId,
                member.project.id
            )
        }
        jdbcTemplate.batchUpdate(sqlUpdate, batchUpdateArgs)
    }

    private fun addNewMembers(members: List<ProjectMember>) {
        val sqlInsert = """
        INSERT INTO project_member (project_id, account_id, role, created_at)
        SELECT ?, ?, ?, ? 
        WHERE NOT EXISTS (
            SELECT 1 FROM project_member WHERE account_id = ? AND project_id = ?
        )
        """
        val batchInsertArgs = members.map { member ->
            arrayOf(
                member.project.id,
                member.accountId,
                member.role.name,
                member.createdAt,
                member.accountId,
                member.project.id
            )
        }
        jdbcTemplate.batchUpdate(sqlInsert, batchInsertArgs)
    }

    override fun updateAll(members: List<ProjectMember>): List<ProjectMember> {
        val sql = """
            UPDATE project_member
            SET role = ?, deleted_at = ?
            WHERE account_id = ? and project_id = ?
        """
        val batchArgs = members.map { projectMember ->
            arrayOf(
                projectMember.role.name,
                projectMember.deletedAt,
                projectMember.accountId,
                projectMember.project.id
            )
        }
        jdbcTemplate.batchUpdate(sql, batchArgs)
        return members
    }

    override fun findByAccountIdAndProjectId(accountId: Long, projectId: Long): ProjectMember? {
        return repository.findByAccountIdAndProjectIdAndDeletedAtIsNull(accountId, projectId)
    }
}

interface JpaProjectMemberRepository: JpaRepository<ProjectMember, Long> {
    fun findByAccountIdAndProjectIdAndDeletedAtIsNull(accountId: Long, projectId: Long): ProjectMember?
}