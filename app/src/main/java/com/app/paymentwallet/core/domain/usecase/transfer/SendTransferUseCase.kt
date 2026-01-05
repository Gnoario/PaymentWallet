package com.app.paymentwallet.core.domain.usecase.transfer

import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Transfer

interface SendTransferUseCase {
    operator fun invoke(
        payerId: String,
        payeeId: String,
        amount: Money
    ): Result<Transfer>
}