package com.app.paymentwallet.core.ports.system

import com.app.paymentwallet.core.domain.model.SeedData

interface SeedDataProvider {
    fun load(): Result<SeedData>
}
