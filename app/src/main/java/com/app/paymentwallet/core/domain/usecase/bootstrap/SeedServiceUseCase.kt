package com.app.paymentwallet.core.domain.usecase.bootstrap

interface SeedServiceUseCase {
    operator fun invoke(): Result<Unit>
}