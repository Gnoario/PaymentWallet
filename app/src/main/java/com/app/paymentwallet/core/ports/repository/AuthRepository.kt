package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.User

interface AuthRepository {
    fun login(email: String, password: String): Result<User>
    fun getSession(): Result<User>
    fun logout(): Result<Unit>
}