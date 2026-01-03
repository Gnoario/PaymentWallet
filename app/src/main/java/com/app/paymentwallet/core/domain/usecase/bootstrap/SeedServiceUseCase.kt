package com.app.paymentwallet.core.domain.usecase.bootstrap

interface SeedServiceUseCase {
    fun invoke(): Result<Unit>
}