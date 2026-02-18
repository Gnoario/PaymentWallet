package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.Transfer

interface TransferRepository {
    fun send(
        transfer: Transfer,
    ): Result<Transfer>
}