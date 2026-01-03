package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Transfer

interface TransferRepository {
    fun invoke(
        payerId: String,
        payeeId: String,
        amount: Money
    ): Result<Transfer>
}