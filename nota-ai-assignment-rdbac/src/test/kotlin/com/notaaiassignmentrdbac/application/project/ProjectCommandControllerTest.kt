package com.notaaiassignmentrdbac.application.project

import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.config.security.CustomUserDto
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.application.project.controller.ProjectCommandController
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectCreateRequest
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectUpdateRequest
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest
import com.notaaiassignmentrdbac.domain.project.entity.Project
import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole
import com.notaaiassignmentrdbac.domain.project.repository.ProjectMemberRepository
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class ProjectCommandControllerTest {
    @Autowired
    private lateinit var projectCommandController: ProjectCommandController

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var projectMemberRepository: ProjectMemberRepository

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Test
    fun `일반 유저는 프로젝트를 만들 수 없다`() {
        // Arrange
        val account = Account.createActiveAccount(
            email = "test@email.com",
            password = "password",
            tenantKey = "tenantKey",
            role = AccountRole.USER
        )
        val savedAccount = accountRepository.save(account)
        val userDetails = CustomUserDetails(
            CustomUserDto(
                accountId = savedAccount.id,
                role = savedAccount.role,
                tenantKey = savedAccount.tenantKey
            )
        )
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        val projectCreateRequest = ProjectCreateRequest(
            title = "title",
            memberRequests = emptyList()
        )

        // Action, Assert
        assertThatThrownBy {
            projectCommandController.createProject(
                userDetails = userDetails,
                request = projectCreateRequest
            )
        }.isInstanceOf(ApplicationException::class.java)
    }

    @Nested
    inner class ADMIN_권한테스트 {
        lateinit var userDetails: CustomUserDetails
        lateinit var savedAdminAccount: Account

        @BeforeEach
        fun setUp() {
            val account = Account.createActiveAccount(
                email = "test@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.ADMIN
            )
            savedAdminAccount = accountRepository.save(account)
            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = savedAdminAccount.id,
                    role = savedAdminAccount.role,
                    tenantKey = savedAdminAccount.tenantKey
                )
            )
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        }

        @Test
        fun `ADMIN 유저는 프로젝트를 만들 수 있다`() {
            // Arrange
            val projectCreateRequest = ProjectCreateRequest(
                title = "title",
                memberRequests = emptyList()
            )

            // Action
            val response = projectCommandController.createProject(
                userDetails = userDetails,
                request = projectCreateRequest
            )
            // Assert
            assertThat(response.data?.projectId).isNotNull()
        }

        @Test
        fun `ADMIN 유저는 프로젝트를 만들때 멤버를 추가할 수 있다`() {
            // Arrange
            val member = Account.createActiveAccount(
                email = "member@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.USER
            )
            val savedMember = accountRepository.save(member)

            val projectCreateRequest = ProjectCreateRequest(
                title = "title",
                memberRequests = listOf(
                    MemberRequest(
                        accountId = savedMember.id,
                        role = ProjectRole.VIEWER
                    )
                )
            )

            // Action
            val response = projectCommandController.createProject(
                userDetails = userDetails,
                request = projectCreateRequest
            )

            // Assert
            val projectMember =
                projectMemberRepository.findByAccountIdAndProjectId(savedMember.id, response.data?.projectId!!)
            assertThat(projectMember).isNotNull()
        }
    }

    @Nested
    inner class PROJECT_OWNER_권한테스트 {
        lateinit var userDetails: CustomUserDetails
        lateinit var savedAdminAccount: Account
        lateinit var invitedProjectOwnerAccount: Account
        lateinit var savedProject: Project

        /**
         * 어드민으로 프로젝트 생성 -> 프로젝트 오너 초대 -> (항목별 테스트)
         */
        @BeforeEach
        fun setUp() {
            // 관리자 생성
            val adminAccount = Account.createActiveAccount(
                email = "test@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.ADMIN
            )
            savedAdminAccount = accountRepository.save(adminAccount)

            // 초대할 오너 계정 생성
            val newProjectOwnerAccount = Account.createActiveAccount(
                email = "owner@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.USER
            )
            invitedProjectOwnerAccount = accountRepository.save(newProjectOwnerAccount)

            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = savedAdminAccount.id,
                    role = savedAdminAccount.role,
                    tenantKey = savedAdminAccount.tenantKey
                )
            )

            // 어드민으로 프로젝트 생성하기위해 인증계정 변경
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

            val projectResponse = projectCommandController.createProject(
                userDetails = CustomUserDetails(
                    CustomUserDto(
                        accountId = savedAdminAccount.id,
                        role = savedAdminAccount.role,
                        tenantKey = savedAdminAccount.tenantKey
                    )
                ),
                request = ProjectCreateRequest(
                    title = "title",
                    memberRequests = listOf(
                        MemberRequest(
                            accountId = invitedProjectOwnerAccount.id,
                            role = ProjectRole.PROJECT_OWNER
                        )
                    )
                )
            )
            savedProject =
                projectRepository.findById(projectResponse.data?.projectId!!) ?: throw Exception("Project is null")

            // 테스트할 오너 계정으로 변경
            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = invitedProjectOwnerAccount.id,
                    role = invitedProjectOwnerAccount.role,
                    tenantKey = invitedProjectOwnerAccount.tenantKey
                )
            )
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        }

        @Test
        fun `PROJECT_OWNER는 프로젝트를 삭제할 수 있다`() {
            // Action
            projectCommandController.deleteProject(
                userDetails = userDetails,
                projectId = savedProject.id
            )
            // Assert
            assertThatThrownBy { projectRepository.getById(savedProject.id) }.isInstanceOf(ApplicationException::class.java)
        }
    }

    @Nested
    inner class EDITOR_권한테스트 {
        lateinit var userDetails: CustomUserDetails
        lateinit var savedAdminAccount: Account
        lateinit var invitedProjectEditorAccount: Account
        lateinit var savedProject: Project
        val oldTitleOfProject = "oldTitle"

        /**
         * 어드민으로 프로젝트 생성 -> 프로젝트 에디터 초대 -> (항목별 테스트)
         */
        @BeforeEach
        fun setUp() {
            // 관리자 생성
            val adminAccount = Account.createActiveAccount(
                email = "test@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.ADMIN
            )
            savedAdminAccount = accountRepository.save(adminAccount)

            // 초대할 에디터 계정 생성
            val newProjectEditorAccount = Account.createActiveAccount(
                email = "editor@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.USER
            )
            invitedProjectEditorAccount = accountRepository.save(newProjectEditorAccount)

            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = savedAdminAccount.id,
                    role = savedAdminAccount.role,
                    tenantKey = savedAdminAccount.tenantKey
                )
            )

            // 어드민으로 프로젝트 생성하기위해 인증계정 변경
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

            val projectResponse = projectCommandController.createProject(
                userDetails = CustomUserDetails(
                    CustomUserDto(
                        accountId = savedAdminAccount.id,
                        role = savedAdminAccount.role,
                        tenantKey = savedAdminAccount.tenantKey
                    )
                ),
                request = ProjectCreateRequest(
                    title = oldTitleOfProject,
                    memberRequests = listOf(
                        MemberRequest(
                            accountId = invitedProjectEditorAccount.id,
                            role = ProjectRole.EDITOR
                        )
                    )
                )
            )
            savedProject =
                projectRepository.findById(projectResponse.data?.projectId!!) ?: throw Exception("Project is null")

            // 테스트할 오너 계정으로 변경
            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = invitedProjectEditorAccount.id,
                    role = invitedProjectEditorAccount.role,
                    tenantKey = invitedProjectEditorAccount.tenantKey
                )
            )
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        }

        @Test
        fun `EDITOR는 프로젝트를 수정 할 수 있다`() {
            // Arrange
            val projectUpdateRequest = ProjectUpdateRequest(
                title = "new title",
            )

            // Action
            projectCommandController.updateProject(
                projectId = savedProject.id,
                userDetails = userDetails,
                request = projectUpdateRequest
            )
            // Assert
            val updatedProject = projectRepository.findById(savedProject.id) ?: throw Exception("Project is null")
            assertThat(oldTitleOfProject).isNotEqualTo(updatedProject.title)
        }
    }
}