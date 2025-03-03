package com.notaaiassignmentrdbac.application.common

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ValidEmailValidatorTest{
    @Test
    fun `빈칸인경우 예외적으로 성공 (필수값이 아니게될 경우를 대비)`(){
        // Arrange
        val emailValidator = ValidEmailValidator()
        // Action
        val result = emailValidator.isValid("", null)
        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `온전한 이메일인경우 성공`(){
        // Arrange
        val emailValidator = ValidEmailValidator()
        // Action
        val result = emailValidator.isValid("abc@def.com", null)
        // Assert
        assertThat(result).isTrue()
    }
    @Test
    fun `아이디만 있는경우 실패`(){
        // Arrange
        val emailValidator = ValidEmailValidator()
        // Action
        val result = emailValidator.isValid("abc", null)
        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `@가 없는 경우 실패`(){
        // Arrange
        val emailValidator = ValidEmailValidator()
        // Action
        val result = emailValidator.isValid("abc.com", null)
        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `1차 도메인이 없는경우 실패`(){
        // Arrange
        val emailValidator = ValidEmailValidator()
        // Action
        val result = emailValidator.isValid("abc@def", null)
        // Assert
        assertThat(result).isFalse()
    }

}