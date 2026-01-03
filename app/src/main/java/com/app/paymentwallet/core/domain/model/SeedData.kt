package com.app.paymentwallet.core.domain.model

data class SeedData(
    val users: List<User>,
    val wallets: List<Wallet>,
    val contacts: List<Contact>
)
