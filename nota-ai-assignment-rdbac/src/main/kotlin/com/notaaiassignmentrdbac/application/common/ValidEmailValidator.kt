package com.notaaiassignmentrdbac.application.common

import com.notaaiassignmentrdbac.application.aop.ValidEmail
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValidEmailValidator : ConstraintValidator<ValidEmail, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true

        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        return value.matches(regex.toRegex())
    }
}