package com.notaaiassignmentrdbac.domain.project.entity

import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Project(
    val ownerId: Long,
    title: String,
    status: ProjectStatus,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    var title: String =title
        private set
    @Enumerated(EnumType.STRING)
    var status: ProjectStatus = status
        private set

    @OneToMany(mappedBy = "project", cascade = [], orphanRemoval = false)
    private val _members: MutableList<ProjectMember> = mutableListOf()
    val members: List<ProjectMember> get() = _members.toList()

    var updatedAt: LocalDateTime? = null
        private set
    var deletedAt: LocalDateTime? = null
        private set

    companion object {
        fun createActiveProject(title: String, ownerId: Long, memberRequests: List<MemberRequest>): Project {
            val project = Project(
                title = title,
                ownerId = ownerId,
                status = ProjectStatus.ACTIVE
            )
            memberRequests.forEach { memberRequest ->
                project.addMember(memberRequest.accountId, memberRequest.role)
            }
            return project
        }
    }

    fun updateTitle(title: String) {
        this.title = title
        updatedAt = LocalDateTime.now()
    }

    fun addMember(accountId: Long, role: ProjectRole = ProjectRole.VIEWER) {
        val member = ProjectMember(accountId, this, role = role)
        _members.add(member)
    }

    fun removeMember(member: ProjectMember) {
        _members.remove(member)
        member.removeFromProject()
    }

    fun delete() {
        status = ProjectStatus.DELETED
        deletedAt = LocalDateTime.now()
    }
}