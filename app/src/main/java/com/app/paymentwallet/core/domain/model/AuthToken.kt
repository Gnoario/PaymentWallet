package com.app.paymentwallet.core.domain.model

data class AuthToken(
    val value: String,
    val createdAtEpochMillis: Long
)
