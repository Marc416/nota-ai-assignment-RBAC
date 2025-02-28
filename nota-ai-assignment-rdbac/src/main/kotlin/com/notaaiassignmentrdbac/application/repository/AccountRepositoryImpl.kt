package com.notaaiassignmentrdbac.application.repository

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.repository.AccountRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryImpl(
    private val repository: JpaAccountRepository
) : AccountRepository {
    override fun save(account: Account): Account {
        return repository.save(account)
    }

    override fun findByEmailAndTenantKeyAndPassword(email: String, password: String, tenantKey: String): Account {
        return repository.findByEmailAndTenantKeyAndPassword(
            email = email,
            tenantKey = tenantKey,
            password = password
        )
            ?: throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "없는 유저 입니다."
            )
    }

}

interface JpaAccountRepository : JpaRepository<Account, Long> {
    fun findByEmailAndTenantKeyAndPassword(email: String, tenantKey: String, password: String): Account?
}