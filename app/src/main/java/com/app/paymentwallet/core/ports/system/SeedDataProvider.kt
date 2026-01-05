package com.app.paymentwallet.core.ports.system

interface SeedDataProvider {
    fun invoke(): Result<Unit>
}
