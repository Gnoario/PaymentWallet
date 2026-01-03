package com.app.paymentwallet.core.network

import com.app.paymentwallet.core.domain.model.NetworkRequest

internal interface ApiInterceptor {
    fun intercept(
        request: NetworkRequest,
        chain: Chain
    ): Response

    interface Chain {
        fun proceed(request: NetworkRequest): Response
    }
}