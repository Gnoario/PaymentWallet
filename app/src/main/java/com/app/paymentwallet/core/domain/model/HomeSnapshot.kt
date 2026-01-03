package com.app.paymentwallet.core.domain.model

data class HomeSnapshot(
    val user: User,
    val wallet: Wallet,
    val contacts: List<Contact>
)
