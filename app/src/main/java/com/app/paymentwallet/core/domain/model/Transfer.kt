package com.app.paymentwallet.core.domain.model

data class Transfer(
    val id: String,
    val payerId: String,
    val payeeId: String,
    val amount: Money,
    val createdAtEpochMillis: Long
)
