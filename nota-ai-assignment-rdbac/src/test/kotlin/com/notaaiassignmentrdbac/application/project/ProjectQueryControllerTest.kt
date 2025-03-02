package com.notaaiassignmentrdbac.application.project

import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.config.security.CustomUserDto
import com.notaaiassignmentrdbac.application.project.controller.ProjectCommandController
import com.notaaiassignmentrdbac.application.project.controller.ProjectQueryController
import com.notaaiassignmentrdbac.application.project.dto.request.ProjectCreateRequest
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import com.notaaiassignmentrdbac.domain.project.dto.request.MemberRequest
import com.notaaiassignmentrdbac.domain.project.entity.Project
import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import org.assertj.core.api.Assertions.assertThat
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
class ProjectQueryControllerTest {
    @Autowired
    private lateinit var projectCommandController: ProjectCommandController

    @Autowired
    private lateinit var projectQueryController: ProjectQueryController

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Nested
    inner class VIEWER_권한테스트 {
        lateinit var userDetails: CustomUserDetails
        lateinit var savedAdminAccount: Account
        lateinit var invitedProjectViewerAccount: Account
        lateinit var savedProject: Project
        val projectTitle = "Title"

        /**
         * 어드민으로 프로젝트 생성 -> 프로젝트 뷰어 초대 -> (항목별 테스트)
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
                email = "viewer@email.com",
                password = "password",
                tenantKey = "tenantKey",
                role = AccountRole.USER
            )
            invitedProjectViewerAccount = accountRepository.save(newProjectEditorAccount)

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
                    title = projectTitle,
                    memberRequests = listOf(
                        MemberRequest(
                            accountId = invitedProjectViewerAccount.id,
                            role = ProjectRole.VIEWER
                        )
                    )
                )
            )
            savedProject =
                projectRepository.findById(projectResponse.data?.projectId!!) ?: throw Exception("Project is null")

            // 테스트할 오너 계정으로 변경
            userDetails = CustomUserDetails(
                CustomUserDto(
                    accountId = invitedProjectViewerAccount.id,
                    role = invitedProjectViewerAccount.role,
                    tenantKey = invitedProjectViewerAccount.tenantKey
                )
            )
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        }

        @Test
        fun `VIEWER는 프로젝트를 조회할 수 있다`() {
            // Action
            val response = projectQueryController.getProjects(
                size = 10
            )
            // Assert
            assertThat(response.data!!.content.size).isGreaterThan(1)
        }
    }
}