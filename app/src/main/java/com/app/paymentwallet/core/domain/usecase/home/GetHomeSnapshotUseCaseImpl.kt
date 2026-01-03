package com.app.paymentwallet.core.domain.usecase.home

import com.app.paymentwallet.core.domain.model.DomainError
import com.app.paymentwallet.core.domain.model.HomeSnapshot
import com.app.paymentwallet.core.ports.storage.ContactsStore
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import com.app.paymentwallet.core.ports.storage.TokenStore
import com.app.paymentwallet.core.ports.storage.WalletStore
import com.app.paymentwallet.framework.di.Container
import kotlin.getValue

class GetHomeSnapshotUseCaseImpl() : GetHomeSnapshotUseCase {

    private val tokenStore: TokenStore by Container.delegate()
    private val userStore: LocalUserStore by Container.delegate()
    private val walletStore: WalletStore by Container.delegate()
    private val contactsStore: ContactsStore by Container.delegate()

    override operator fun invoke(): Result<HomeSnapshot> = runCatching {

        val token = tokenStore.get().getOrElse { throw it }
        if (token == null) throw DomainError.NotAuthenticated

        val user = userStore.get().getOrElse { throw it }
        if (user == null) throw DomainError.NotAuthenticated

        val wallet = walletStore.getWallet(user.id).getOrElse { throw it }
        val contacts = contactsStore.getContacts().getOrElse { throw it }

        HomeSnapshot(
            user = user,
            wallet = wallet,
            contacts = contacts
        )
    }
}
