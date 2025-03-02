package com.notaaiassignmentrdbac.application.aop

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole
import com.notaaiassignmentrdbac.domain.project.repository.ProjectMemberRepository
import com.notaaiassignmentrdbac.domain.project.repository.ProjectRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable

@Aspect
@Component
class CheckProjectRoleAspect(
    private val projectRepository: ProjectRepository,
    private val projectMemberRepository: ProjectMemberRepository
) {

    @Around("@annotation(CheckProjectRole) && @annotation(checkProjectRole)")
    @Throws(Throwable::class)
    fun checkUserRole(joinPoint: ProceedingJoinPoint, checkProjectRole: CheckProjectRole): Any {
        val authentication: org.springframework.security.core.Authentication? =
            SecurityContextHolder.getContext().authentication
        val customUserDetails = authentication?.principal as CustomUserDetails

        // VIEWER는 모든 접근 허용
        if (checkProjectRole.value == ProjectRole.VIEWER) {
            return joinPoint.proceed()
        }

        val projectId = extractProjectId(joinPoint)

        // 프로젝트 ID가 없으면 ADMIN 권한을 체크
        if (projectId == null) {
            if (AccountRole.ADMIN !in customUserDetails.user.role.getAllRoles()) {
                throw ApplicationException(
                    code = CodeEnum.FRS_002,
                    message = "프로젝트를 생성할 권한이 없습니다.",
                    data = mapOf(
                        "requiredRole" to checkProjectRole.value
                    )
                )
            }
            return joinPoint.proceed()
        }

        // 기존 프로젝트 권한 체크
        if (!hasAuthority(customUserDetails, projectId, checkProjectRole.value)) {
            throw ApplicationException(
                CodeEnum.FRS_002,
                "프로젝트에 대한 권한이 없습니다.",
                data = mapOf(
                    "requiredRole" to checkProjectRole.value,
                    "projectId" to projectId
                )
            )
        }

        return joinPoint.proceed()
    }

    private fun extractProjectId(joinPoint: ProceedingJoinPoint): Long? {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val params = method.parameters

        for (i in params.indices) {
            if (params[i].isAnnotationPresent(PathVariable::class.java)) {
                return joinPoint.args[i] as? Long
            }
        }
        return null
    }

    private fun hasAuthority(
        customUserDetails: CustomUserDetails,
        projectId: Long,
        requiredRole: ProjectRole
    ): Boolean {
        // ADMIN 권한이 있으면 무조건 허용
        if (AccountRole.ADMIN in customUserDetails.user.role.getAllRoles()) {
            return true
        }

        // 프로젝트 정보 조회
        val project = projectRepository.findById(projectId) ?: return false

        if (requiredRole == ProjectRole.PROJECT_OWNER && project.ownerId == customUserDetails.user.accountId) {
            return true
        }

        // 사용자가 project_member 테이블에서 권한을 가지고 있는지 확인
        val projectMember =
            projectMemberRepository.findByAccountIdAndProjectId(customUserDetails.user.accountId, projectId)
                ?: return false

        // 사용자가 requiredRole을 포함하는지 확인 (상속된 역할 포함)
        return requiredRole in projectMember.role.getAllRoles()
    }
}