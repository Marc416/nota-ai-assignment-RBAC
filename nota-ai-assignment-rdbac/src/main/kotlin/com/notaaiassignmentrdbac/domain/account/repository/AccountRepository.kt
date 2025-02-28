package com.notaaiassignmentrdbac.domain.account.repository

import com.notaaiassignmentrdbac.domain.account.entity.Account

interface AccountRepository {
    fun save(account: Account): Account
    fun findByEmailAndTenantKeyAndPassword(email: String, password: String, tenantKey: String): Account
}