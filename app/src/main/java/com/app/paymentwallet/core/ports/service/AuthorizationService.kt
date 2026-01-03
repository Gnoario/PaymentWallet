package com.app.paymentwallet.core.ports.service

import com.app.paymentwallet.core.domain.model.AuthorizationResult
import com.app.paymentwallet.core.domain.model.Money

interface AuthorizationService {
    fun authorize(value: Money): Result<AuthorizationResult>
}
