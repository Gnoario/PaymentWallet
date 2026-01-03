package com.app.paymentwallet.core.domain.model

internal data class NetworkRequest(
    val method: String,
    val url: String,
    val body: String?,
)