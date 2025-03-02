package com.notaaiassignmentrdbac.domain.project.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDateTime

@Entity
@BatchSize(size = 50)
class ProjectMember(
    val accountId: Long,
    @ManyToOne
    @JoinColumn(name = "project_id")
    val project: Project,
    @Enumerated(EnumType.STRING)
    val role: ProjectRole,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    var deletedAt: LocalDateTime? = null
        private set

    fun removeFromProject() {
        deletedAt = LocalDateTime.now()
    }
}