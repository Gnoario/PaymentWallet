package com.app.paymentwallet.core.domain.usecase.auth

import com.app.paymentwallet.core.domain.model.User

interface LoginUseCase {
    operator fun invoke(email: String, password: String): Result<User>
}