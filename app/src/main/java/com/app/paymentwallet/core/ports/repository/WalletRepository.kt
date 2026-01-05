package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.Wallet

interface WalletRepository {
    fun get(userId: String): Result<Wallet>
}