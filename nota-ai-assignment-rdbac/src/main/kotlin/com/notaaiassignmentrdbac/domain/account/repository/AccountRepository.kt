package com.notaaiassignmentrdbac.domain.account.repository

import com.notaaiassignmentrdbac.domain.account.entity.Account

interface AccountRepository {
    fun save(account: Account): Account
    fun findByAccountId(accountId: Long): Account
    fun findByEmailAndTenantKeyAndPassword(email: String, tenantKey: String, password: String): Account
}