package com.app.paymentwallet.core.domain.usecase.login

import com.app.paymentwallet.core.domain.model.AuthToken
import com.app.paymentwallet.core.domain.model.User
import com.app.paymentwallet.core.ports.repository.AuthRepository
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import com.app.paymentwallet.core.ports.storage.TokenStore
import com.app.paymentwallet.core.ports.system.Clock
import com.app.paymentwallet.core.ports.system.TokenGenerator
import com.app.paymentwallet.framework.di.Container
import kotlin.getValue

class LoginUseCaseImpl(
) : LoginUseCase {

    private val authRepository: AuthRepository by Container.delegate()
    private val tokenStore: TokenStore by Container.delegate()
    private val userStore: LocalUserStore by Container.delegate()
    private val tokenGenerator: TokenGenerator by Container.delegate()
    private val clock: Clock by Container.delegate()

    override operator fun invoke(
        email: String,
        password: String
    ): Result<User> = runCatching {

        val user = authRepository.login(email, password)
            .getOrElse { throw it }

        val token = AuthToken(
            value = tokenGenerator.generate(),
            createdAtEpochMillis = clock.nowEpochMillis()
        )

        tokenStore.save(token).getOrElse { throw it }
        userStore.save(user).getOrElse { throw it }

        user
    }
}
