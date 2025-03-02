package com.notaaiassignmentrdbac.application.aop

import com.notaaiassignmentrdbac.domain.project.entity.ProjectRole

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckProjectRole(val value: ProjectRole)