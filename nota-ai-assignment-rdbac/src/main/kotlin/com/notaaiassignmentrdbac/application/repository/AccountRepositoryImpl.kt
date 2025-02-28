package com.notaaiassignmentrdbac.application.repository

import com.notaaiassignmentrdbac.application.common.httpresponse.CodeEnum
import com.notaaiassignmentrdbac.application.exception.ApplicationException
import com.notaaiassignmentrdbac.domain.account.entity.Account
import com.notaaiassignmentrdbac.domain.account.entity.Status
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

    override fun findByUserId(userId: Long): Account {
        return repository.findByIdAndStatus(userId, Status.ACTIVE)
            ?: throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "없는 유저 입니다."
            )
    }

    override fun findByEmailAndTenantKeyAndPassword(email: String, tenantKey: String, password: String): Account {
        return repository.findByEmailAndTenantKeyAndPasswordAndStatus(
            email = email,
            tenantKey = tenantKey,
            password = password,
            status = Status.ACTIVE
        )
            ?: throw ApplicationException(
                code = CodeEnum.FRS_001,
                message = "없는 유저 입니다."
            )
    }

}

interface JpaAccountRepository : JpaRepository<Account, Long> {
    fun findByIdAndStatus(userId: Long, status: Status): Account?
    fun findByEmailAndTenantKeyAndPasswordAndStatus(
        email: String,
        tenantKey: String,
        password: String,
        status: Status
    ): Account?
}