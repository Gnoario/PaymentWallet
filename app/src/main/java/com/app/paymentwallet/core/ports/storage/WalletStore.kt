package com.app.paymentwallet.core.ports.storage

import com.app.paymentwallet.core.domain.model.Wallet

interface WalletStore {
    fun getWallet(userId: String): Result<Wallet>
    fun updateWallet(wallet: Wallet): Result<Unit>
}
