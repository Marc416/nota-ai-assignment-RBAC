package com.notaaiassignmentrdbac.application.aop

import com.notaaiassignmentrdbac.application.common.ValidEmailValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ValidEmailValidator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEmail(
    val message: String = "유효하지 않은 이메일 형식입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)