package com.app.paymentwallet.core.domain.usecase.bootstrap

import com.app.paymentwallet.core.ports.storage.SeedFlagStore
import com.app.paymentwallet.core.ports.system.SeedDataProvider
import com.app.paymentwallet.framework.di.Container

class SeedServiceUseCaseImpl() : SeedServiceUseCase {

    private val seedDataProvider: SeedDataProvider by Container.delegate()
    private val seedFlagStore: SeedFlagStore by Container.delegate()

    override fun invoke(): Result<Unit> = runCatching {

        if (seedFlagStore.isSeeded()) return@runCatching

        seedDataProvider.invoke().getOrElse { throw it }

        seedFlagStore.markSeeded()
    }
}
