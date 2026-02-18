package com.app.paymentwallet.core.domain.usecase.transfer

import com.app.paymentwallet.core.domain.model.DomainError
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Transfer
import com.app.paymentwallet.core.ports.repository.TransferRepository
import com.app.paymentwallet.core.ports.repository.WalletRepository
import com.app.paymentwallet.core.ports.system.Clock
import com.app.paymentwallet.core.ports.system.IdGenerator
import com.app.paymentwallet.framework.di.Container

class SendTransferUseCaseImpl : SendTransferUseCase {
    private val walletRepository: WalletRepository by Container.delegate()
    private val idGenerator: IdGenerator by Container.delegate()
    private val transferRepository: TransferRepository by Container.delegate()
    private val clock: Clock by Container.delegate()

    override operator fun invoke(
        payerId: String,
        payeeId: String,
        amount: Money
    ): Result<Transfer> = runCatching {

        if (amount.isZeroOrLess()) throw DomainError.InvalidAmount
        if (payerId == payeeId) throw DomainError.SamePayerAndPayee

        val wallet = walletRepository.get(payerId).getOrElse { throw it }
        if (wallet.balance.isLessThan(amount)) {
            throw DomainError.InsufficientBalance
        }

        val transfer = Transfer(
            id = idGenerator.new(),
            payerId = payerId,
            payeeId = payeeId,
            amount = amount,
            createdAtEpochMillis = clock.nowEpochMillis()
        )

        val result = transferRepository.send(transfer)
            .getOrElse { throw it }

        result
    }
}
