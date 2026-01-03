package com.app.paymentwallet.core.domain.usecase.transfer

import com.app.paymentwallet.core.domain.model.DomainError
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Transfer
import com.app.paymentwallet.core.ports.service.AuthorizationService
import com.app.paymentwallet.core.ports.service.NotificationService
import com.app.paymentwallet.core.ports.storage.TokenStore
import com.app.paymentwallet.core.ports.storage.WalletStore
import com.app.paymentwallet.core.ports.system.Clock
import com.app.paymentwallet.core.ports.system.IdGenerator
import com.app.paymentwallet.framework.di.Container

class TransferUseCaseImpl : TransferUseCase {

    private val tokenStore: TokenStore by Container.delegate()
    private val walletStore: WalletStore by Container.delegate()
    private val authorizationService: AuthorizationService by Container.delegate()
    private val notificationService: NotificationService by Container.delegate()
    private val idGenerator: IdGenerator by Container.delegate()
    private val clock: Clock by Container.delegate()

    override operator fun invoke(
        payerId: String,
        payeeId: String,
        amount: Money
    ): Result<Transfer> = runCatching {

        val token = tokenStore.get().getOrElse { throw it }
        if (token == null) throw DomainError.NotAuthenticated

        if (amount.isZeroOrLess()) throw DomainError.InvalidAmount
        if (payerId == payeeId) throw DomainError.SamePayerAndPayee

        val wallet = walletStore.getWallet(payerId).getOrElse { throw it }
        if (wallet.balance.isLessThan(amount)) {
            throw DomainError.InsufficientBalance
        }

        val authorization = authorizationService.authorize(amount)
            .getOrElse { throw it }

        if (!authorization.authorized) {
            throw DomainError.AuthorizationDenied(authorization.reason)
        }

        val updatedWallet = wallet.copy(
            balance = wallet.balance - amount
        )
        walletStore.updateWallet(updatedWallet).getOrElse { throw it }

        val transfer = Transfer(
            id = idGenerator.newId(),
            payerId = payerId,
            payeeId = payeeId,
            amount = amount,
            createdAtEpochMillis = clock.nowEpochMillis()
        )

        notificationService.notifyTransferCompleted(transfer)
            .getOrElse { throw it }

        transfer
    }
}
