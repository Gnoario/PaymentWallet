package com.app.paymentwallet.core.ports.storage

import com.app.paymentwallet.core.domain.model.AuthToken

interface TokenStore {
    fun save(token: AuthToken): Result<Unit>
    fun get(): Result<AuthToken?>
    fun clear(): Result<Unit>
}
