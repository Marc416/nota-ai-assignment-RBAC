package com.notaaiassignmentrdbac.application.aop

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.Role
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Aspect
@Component
class CheckRoleAspect {
    @Around("@annotation(CheckRole) && @annotation(checkRole)")
    @Throws(Throwable::class)
    fun checkUserRole(joinPoint: ProceedingJoinPoint, checkRole: CheckRole): Any {
        val requiredRole: Role = checkRole.value
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        val customUserDetails = authentication?.principal as? CustomUserDetails

        if (customUserDetails == null || !checkAuthority(requiredRole, customUserDetails)) {
            throw ApplicationException(code = CodeEnum.FRS_002, message = CodeEnum.FRS_002.description)
        }

        return joinPoint.proceed()
    }

    private fun checkAuthority(requiredRole: Role, customUserDetails: CustomUserDetails): Boolean {
        return requiredRole in customUserDetails.user.role.getAllRoles()
    }
}