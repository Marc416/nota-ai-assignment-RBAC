package com.notaaiassignmentrdbac.application.aop

import com.notaaiassignmentrdbac.domain.account.entity.Role
import java.util.*


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckRole(val value: Role)