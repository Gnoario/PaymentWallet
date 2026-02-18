package com.app.paymentwallet.core.ports.storage

import com.app.paymentwallet.core.domain.model.User

interface LocalUserStore {
    fun save(user: User): Result<Unit>
    fun get(): Result<User?>
    fun clear(): Result<Unit>
}
