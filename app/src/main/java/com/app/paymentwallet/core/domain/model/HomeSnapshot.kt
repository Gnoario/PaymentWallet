package com.app.paymentwallet.core.domain.model

data class HomeSnapshot(
    val wallet: Wallet,
    val contacts: List<Contact>
)
