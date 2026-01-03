package com.app.paymentwallet.core.ports.system

interface Clock {
    fun nowEpochMillis(): Long
}
