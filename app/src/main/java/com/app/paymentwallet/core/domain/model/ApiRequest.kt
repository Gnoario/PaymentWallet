package com.app.paymentwallet.core.domain.model

internal data class ApiRequest(
    val method: String,
    val url: String,
    val body: String?,
)