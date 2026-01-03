package com.app.paymentwallet.core.ports.storage

interface SeedFlagStore {
    fun isSeeded(): Boolean
    fun markSeeded()
}
