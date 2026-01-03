package com.app.paymentwallet.core.domain.usecase.login

import com.app.paymentwallet.core.domain.model.User

interface LoginUseCase {
    operator fun invoke(email: String, password: String): Result<User>
}