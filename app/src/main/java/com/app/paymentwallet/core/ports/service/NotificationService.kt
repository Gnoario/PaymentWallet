package com.app.paymentwallet.core.ports.service

import com.app.paymentwallet.core.domain.model.Transfer

interface NotificationService {
    fun notifyTransferCompleted(transfer: Transfer): Result<Unit>
}
