package com.notaaiassignmentrdbac.application.account

import com.notaaiassignmentrdbac.application.account.controller.AccountCommandController
import com.notaaiassignmentrdbac.application.account.dto.request.AccountSignInRequest
import com.notaaiassignmentrdbac.application.account.dto.request.AccountSignupRequest
import com.notaaiassignmentrdbac.application.account.dto.request.ChangePasswordRequest
import com.notaaiassignmentrdbac.application.config.security.CustomUserDetails
import com.notaaiassignmentrdbac.application.config.security.CustomUserDto
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.AccountRole
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class AccountCommandControllerTest {

    @Autowired
    private lateinit var accountCommandController: AccountCommandController

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `유저 가입(Role User)`() {
        // Arrange
        val accountSignupRequest = AccountSignupRequest(
            email = "test@email.com",
            password = "password",
            tenantKey = "tenantKey",
            role = AccountRole.USER
        )
        // Action
        val response = accountCommandController.signUp(
            requestBody = accountSignupRequest
        )
        // Assert
        assertThat(response.data?.id).isNotNull()
        accountRepository.findByAccountId(response.data!!.id).let {
            assertThat(it.role).isEqualTo(AccountRole.USER)
        }
    }

    @Test
    fun `유저 가입(Role Admin)`() {
        // Arrange
        val accountSignupRequest = AccountSignupRequest(
            email = "test@email.com",
            password = "password",
            tenantKey = "tenantKey",
            role = AccountRole.ADMIN
        )
        // Action
        val response = accountCommandController.signUp(
            requestBody = accountSignupRequest
        )
        // Assert
        assertThat(response.data?.id).isNotNull()
        accountRepository.findByAccountId(response.data!!.id).let {
            assertThat(it.role).isEqualTo(AccountRole.ADMIN)
        }
    }

    @Test
    fun `로그인`(){
        // Arrange
        val email = "test@email.com"
        val password = "password"
        val tenantKey = "tenantKey"
        val accountSignupRequest = AccountSignupRequest(
            email = email,
            password = password,
            tenantKey = tenantKey,
            role = AccountRole.ADMIN
        )
        accountCommandController.signUp(
            requestBody = accountSignupRequest
        )
        val accountSignInRequest = AccountSignInRequest(
            email = email,
            tenantKey = tenantKey,
            password = password
        )
        // Action
        val response = accountCommandController.signIn(
            requestBody = accountSignInRequest
        )
        // Assert
        assertThat(response.data?.token).isNotNull()
    }

    @Test
    fun `비밀번호 변경`(){
        val email = "test@email.com"
        val oldPassword = "password"
        val newPassword = "newPassword"
        val tenantKey = "tenantKey"
        val accountSignupRequest = AccountSignupRequest(
            email = email,
            password = oldPassword,
            tenantKey = tenantKey,
            role = AccountRole.ADMIN
        )
        val accountResponse = accountCommandController.signUp(
            requestBody = accountSignupRequest
        )
        val accountId = accountResponse.data?.id ?: throw Exception("Account id is null")
        val account = accountRepository.findByAccountId(accountId)

        val userDetails = CustomUserDetails(
            CustomUserDto(
                accountId = accountId,
                role = account.role,
                tenantKey = account.tenantKey
            )
        )

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        // Action
        accountCommandController.changePassword(
            request = ChangePasswordRequest(newPassword = newPassword),
            userDetails = userDetails
        )

        // Assert
        val changedPasswordAccount = accountRepository.findByAccountId(accountId)
        assertThat(changedPasswordAccount.password).isNotEqualTo(oldPassword)
    }

    @Test
    fun `유저 삭제`(){
        // Arrange
        val email = "test@email.com"
        val oldPassword = "password"
        val tenantKey = "tenantKey"
        val accountSignupRequest = AccountSignupRequest(
            email = email,
            password = oldPassword,
            tenantKey = tenantKey,
            role = AccountRole.ADMIN
        )
        val accountResponse = accountCommandController.signUp(
            requestBody = accountSignupRequest
        )
        val accountId = accountResponse.data?.id ?: throw Exception("Account id is null")
        val account = accountRepository.findByAccountId(accountId)

        val userDetails = CustomUserDetails(
            CustomUserDto(
                accountId = accountId,
                role = account.role,
                tenantKey = account.tenantKey
            )
        )

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        // Action
        accountCommandController.deleteAccount(accountId)
        // Assert
        assertThatThrownBy { accountRepository.findByAccountId(accountId) }
            .isInstanceOf(ApplicationException::class.java)
    }
}