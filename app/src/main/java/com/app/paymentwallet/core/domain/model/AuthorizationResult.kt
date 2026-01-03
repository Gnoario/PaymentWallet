package com.app.paymentwallet.core.domain.model

data class AuthorizationResult(
    val authorized: Boolean,
    val reason: String? = null
)
