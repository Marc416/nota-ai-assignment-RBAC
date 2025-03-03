package com.notaaiassignmentrdbac.application.account.repository

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.AccountStatus
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

    override fun findByAccountId(accountId: Long): Account {
        return repository.findByIdAndStatus(accountId, AccountStatus.ACTIVE)
            ?: throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "없는 유저 입니다."
            )
    }

    override fun findByEmailAndTenantKey(email: String, tenantKey: String): Account {
        return repository.findByEmailAndTenantKeyAndStatus(
            email = email,
            tenantKey = tenantKey,
            status = AccountStatus.ACTIVE
        )
            ?: throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "없는 유저 입니다."
            )
    }

}

interface JpaAccountRepository : JpaRepository<Account, Long> {
    fun findByIdAndStatus(userId: Long, status: AccountStatus): Account?
    fun findByEmailAndTenantKeyAndStatus(
        email: String,
        tenantKey: String,
        status: AccountStatus
    ): Account?
}