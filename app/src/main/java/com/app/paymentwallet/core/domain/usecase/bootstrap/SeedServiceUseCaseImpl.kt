package com.app.paymentwallet.core.domain.usecase.bootstrap

import com.app.paymentwallet.core.ports.storage.ContactsStore
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import com.app.paymentwallet.core.ports.storage.SeedFlagStore
import com.app.paymentwallet.core.ports.storage.WalletStore
import com.app.paymentwallet.core.ports.system.SeedDataProvider
import com.app.paymentwallet.framework.di.Container

class SeedServiceUseCaseImpl() : SeedServiceUseCase {

    private val seedDataProvider: SeedDataProvider by Container.delegate()
    private val userStore: LocalUserStore by Container.delegate()
    private val walletStore: WalletStore by Container.delegate()
    private val contactsStore: ContactsStore by Container.delegate()
    private val seedFlagStore: SeedFlagStore by Container.delegate()

    override fun invoke(): Result<Unit> = runCatching {

        if (seedFlagStore.isSeeded()) return@runCatching

        val seed = seedDataProvider.load().getOrElse { throw it }

        seed.users.forEach { userStore.save(it).getOrElse { throw it } }
        seed.wallets.forEach { walletStore.updateWallet(it).getOrElse { throw it } }

        contactsStore.saveAll(seed.contacts).getOrElse { throw it }

        seedFlagStore.markSeeded()
    }
}
