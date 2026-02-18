package com.app.paymentwallet.core.domain.usecase.auth

import com.app.paymentwallet.core.domain.model.User
import com.app.paymentwallet.core.ports.repository.AuthRepository
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import com.app.paymentwallet.framework.di.Container
import kotlin.getValue

class LoginUseCaseImpl(
) : LoginUseCase {

    private val authRepository: AuthRepository by Container.delegate()
    private val userStore: LocalUserStore by Container.delegate()

    override operator fun invoke(
        email: String,
        password: String
    ): Result<User> = runCatching {

        val user = authRepository.login(email, password)
            .getOrElse { throw it }

        userStore.save(user).getOrElse { throw it }

        user
    }
}
