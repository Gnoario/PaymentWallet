package com.app.paymentwallet.core.network

import com.app.paymentwallet.core.domain.model.ApiRequest

internal interface ApiInterceptor {
    fun invoke(
        request: ApiRequest,
        chain: Chain
    ): Response

    interface Chain {
        fun proceed(request: ApiRequest): Response
    }
}