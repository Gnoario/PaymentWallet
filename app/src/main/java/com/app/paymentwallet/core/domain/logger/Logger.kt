package com.app.paymentwallet.core.domain.logger

internal interface Logger {
    fun info(message: String)
    fun debug(message: String)
    fun error(message: String, throwable: Throwable? = null)
    fun warn(message: String)
}